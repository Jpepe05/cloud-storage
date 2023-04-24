package com.ti5.cloudstorage.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class BackupService {

  private final Set<File> watchedDirectories;
  private final FileService fileService;

  @EventListener(ApplicationReadyEvent.class)
  public void sync() {

    Set<File> awsFiles = fileService.getAllFiles();
    Set<File> localFiles = getLocalFiles();

    log.info("Aws files: {}", awsFiles);
    log.info("Local files: {}", localFiles);

    Set<File> filesToUpload = new HashSet<>(localFiles);
    filesToUpload.removeAll(awsFiles);

    Set<File> filesToDelete = new HashSet<>(awsFiles);
    filesToDelete.removeAll(localFiles);

    log.info("Files to upload: {}", filesToUpload);
    if (!filesToUpload.isEmpty()) {
      fileService.uploadFiles(filesToUpload);
    }

    log.info("Files to delete: {}", filesToDelete);
    if (!filesToDelete.isEmpty()) {
      fileService.deleteFiles(filesToDelete);
    }
  }

  private Set<File> getLocalFiles() {
    return watchedDirectories.stream()
        .map(File::listFiles)
        .filter(Objects::nonNull)
        .flatMap(Arrays::stream)
        .filter(File::isFile)
        .collect(Collectors.toSet());
  }
}
