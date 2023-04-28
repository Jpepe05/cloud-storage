package com.ti5.cloudstorage.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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

@Configuration
public class FilesToWatchConfig {

  private @Value("${backup.root-directory.path:#{null}}") String rootDirectoryPath;

  @Bean
  public Set<File> getWatchedDirectories(Path rootDirectory) throws IOException {

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
  }

  @Bean
  public Path getRootDirectoryPath() {

    File defaultRootDirectory =
        new File(System.getProperty("user.home") + File.separator + "Imagens");

    return Optional.ofNullable(rootDirectoryPath)
        .map(File::new)
        .orElse(defaultRootDirectory)
        .toPath();
  }
}
