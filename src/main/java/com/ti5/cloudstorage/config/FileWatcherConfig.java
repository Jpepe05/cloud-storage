package com.ti5.cloudstorage.config;

import com.ti5.cloudstorage.listener.FileMonitorListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.devtools.filewatch.FileSystemWatcher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.nio.file.Path;

import java.time.Duration;

@Configuration
@Slf4j
public class FileWatcherConfig {

  @Bean(destroyMethod = "stop")
  public FileSystemWatcher fileSystemWatcher(
      FileMonitorListener fileMonitorListener, Path rootDirectory) {
    FileSystemWatcher fileSystemWatcher =
        new FileSystemWatcher(false, Duration.ofMillis(2000L), Duration.ofMillis(1000L));

    fileSystemWatcher.addSourceDirectory(rootDirectory.toFile());
    fileSystemWatcher.addListener(fileMonitorListener);

    return fileSystemWatcher;
  }
}
