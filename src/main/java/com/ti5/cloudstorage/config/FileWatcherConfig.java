package com.ti5.cloudstorage.config;

import com.ti5.cloudstorage.listener.FileMonitorListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.devtools.filewatch.FileSystemWatcher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class FileWatcherConfig {

  private final FileMonitorListener fileMonitorListener;

  @Bean
  public FileSystemWatcher fileSystemWatcher(Path rootDirectory, Set<File> watchedDirectories) {
    FileSystemWatcher fileSystemWatcher =
        new FileSystemWatcher(false, Duration.ofMillis(2000L), Duration.ofMillis(1000L));

    fileSystemWatcher.addSourceDirectory(rootDirectory.toFile());
    fileSystemWatcher.addListener(fileMonitorListener);
    fileSystemWatcher.start();

    log.info("Started fileSystemWatcher on directories {}", watchedDirectories);
    return fileSystemWatcher;
  }

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
  public Path getRootDirectoryPath(ApplicationArguments args) {

    return new File(
            args.getNonOptionArgs().stream()
                .findAny()
                .orElseGet(() -> System.getProperty("user.home") + File.separator + "Imagens"))
        .toPath();
  }
}
