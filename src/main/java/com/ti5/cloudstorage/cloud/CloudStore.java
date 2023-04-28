package com.ti5.cloudstorage.cloud;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.io.File;

@Repository
@RequiredArgsConstructor
@Slf4j
public class CloudStore {

  private final AmazonS3 amazonS3;
  private static final String BUCKET_NAME = "jpepe-ti5-teste-bucket";

  public void uploadFile(File file) {
    log.info("Uploading file {}", file);
    amazonS3.putObject(BUCKET_NAME, file.getPath(), file);
    log.info("File uploaded {}", file);
  }

  public void deleteFile(File file) {
    log.info("Deleting file {}", file);
    amazonS3.deleteObject(BUCKET_NAME, file.getPath());
    log.info("File deleted {}", file);
  }

  public S3Object getFile(File file) {
    log.info("Getting file {}", file);
    return amazonS3.getObject(BUCKET_NAME, file.getPath());
  }

  public ObjectListing getAllObjects() {
    log.info("Getting all objects");
    return amazonS3.listObjects(BUCKET_NAME);
  }
}
