package com.ti5.cloudstorage.service;

import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.ti5.cloudstorage.config.UserConfigs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.boot.devtools.filewatch.ChangedFile;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class BackupService {

  private final UserConfigs userConfigs;
  private final FileService fileService;

  public void sync() {

    AllFiles files = getAllFiles();
    Set<File> filesOnlyLocally = files.filesOnlyLocally();
    Set<File> filesOnlyOnAws = files.filesOnlyOnAws();

    if (!filesOnlyLocally.isEmpty() || !filesOnlyOnAws.isEmpty()) {
      log.info("Syncing files on AWS on thread {}", Thread.currentThread().getId());
      uploadFiles(filesOnlyLocally);
      deleteFiles(filesOnlyOnAws);
    } else {
      log.info("No files to sync");
    }
  }

  public void download() {

    AllFiles files = getAllFiles();
    Set<File> filesOnlyOnAws = files.filesOnlyOnAws();

    if (!filesOnlyOnAws.isEmpty()) {
      log.info("Downloading files on AWS on thread {}", Thread.currentThread().getId());
      downloadFiles(filesOnlyOnAws);
    } else {
      log.info("No files to download");
    }
  }

  private AllFiles getAllFiles() {
    Set<File> awsFiles = fileService.getAllFiles();
    Set<File> localFiles = getLocalFiles();

    log.info("Aws files: {}", awsFiles);
    log.info("Local files: {}", localFiles);

    Set<File> filesOnlyLocally = new HashSet<>(localFiles);
    filesOnlyLocally.removeAll(awsFiles);

    Set<File> filesOnlyOnAws = new HashSet<>(awsFiles);
    filesOnlyOnAws.removeAll(localFiles);
    return new AllFiles(filesOnlyLocally, filesOnlyOnAws);
  }

  private record AllFiles(Set<File> filesOnlyLocally, Set<File> filesOnlyOnAws) {}

  private void uploadFiles(Set<File> filesToUpload) {
    if (!filesToUpload.isEmpty()) {
      log.info("Files to upload: {}", filesToUpload);
      fileService.uploadFiles(filesToUpload);
    }
  }

  private void deleteFiles(Set<File> filesToDelete) {
    if (!filesToDelete.isEmpty()) {
      log.info("Files to delete: {}", filesToDelete);
      fileService.deleteFiles(filesToDelete);
    }
  }

  private void downloadFiles(Set<File> filesToDownload) {
    if (!filesToDownload.isEmpty()) {
      log.info("Files to download: {}", filesToDownload);
      filesToDownload.forEach(
          file -> {
            S3ObjectInputStream inputStream = fileService.getFileContent(file);
            try {
              FileUtils.copyInputStreamToFile(inputStream, new File(file.getPath()));
            } catch (IOException e) {
              log.error("Error while copying file", e);
            }
          });
    }
  }

  private Set<File> getLocalFiles() {
    return userConfigs.getWatchedDirectories().stream()
        .map(File::listFiles)
        .filter(Objects::nonNull)
        .flatMap(Arrays::stream)
        .filter(File::isFile)
        .collect(Collectors.toSet());
  }

  public void sync(ChangedFile changedFile) {
    var fileType = changedFile.getType();
    var file = changedFile.getFile();

    switch (fileType) {
      case ADD -> fileService.uploadFile(file);
      case MODIFY -> fileService.updateFile(file);
      case DELETE -> fileService.deleteFile(file);
    }
  }
}
