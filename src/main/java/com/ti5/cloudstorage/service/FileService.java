package com.ti5.cloudstorage.service;

import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.ti5.cloudstorage.cloud.CloudStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileService {

  private final CloudStore cloudStore;

  public void uploadFiles(Collection<File> files) {
    files.forEach(this::uploadFile);
  }

  public void deleteFiles(Collection<File> files) {
    files.forEach(this::deleteFile);
  }

  public void uploadFile(File file) {
    cloudStore.uploadFile(file);
  }

  public void updateFile(File file) {
    cloudStore.uploadFile(file);
  }

  public void deleteFile(File file) {
    cloudStore.deleteFile(file);
  }

  public S3ObjectInputStream getFileContent(File file) {
    return cloudStore.getFile(file).getObjectContent();
  }

  public Set<File> getAllFiles() {
    ObjectListing objectListing = cloudStore.getAllObjects();
    return objectListing.getObjectSummaries().stream()
        .map(S3ObjectSummary::getKey)
        .map(File::new)
        .collect(Collectors.toSet());
  }
}
