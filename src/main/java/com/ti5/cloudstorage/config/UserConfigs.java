package com.ti5.cloudstorage.config;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
@Getter
public class UserConfigs {

  private Set<File> watchedDirectories;
  private Path rootDirectoryPath;
  private String awsBucketName;

  public UserConfigs() {
    File defaultRootDirectory =
        new File(System.getProperty("user.home") + File.separator + "Imagens");

    this.rootDirectoryPath = defaultRootDirectory.toPath();
    this.watchedDirectories = getWatchedDirectories(rootDirectoryPath);
    this.awsBucketName = "jpepe-ti5-teste-bucket";
  }

  public void setRootDirectoryPath(String rootDirectoryPath) {
    this.rootDirectoryPath =
        Optional.ofNullable(rootDirectoryPath)
            .map(File::new)
            .orElse(this.rootDirectoryPath.toFile())
            .toPath();

    this.watchedDirectories = getWatchedDirectories(this.rootDirectoryPath);
  }

  public void setAwsBucketName(String awsBucketName) {
    this.awsBucketName = awsBucketName;
  }

  private Set<File> getWatchedDirectories(Path rootDirectory) {

    try {
      Set<File> folders = new HashSet<>();
      Files.walkFileTree(
          rootDirectory,
          new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
              folders.add(dir.toFile());
              return FileVisitResult.CONTINUE;
            }
          });

      return folders;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
