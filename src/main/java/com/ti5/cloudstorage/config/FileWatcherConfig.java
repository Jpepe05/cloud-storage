package com.ti5.cloudstorage.config;

import com.ti5.cloudstorage.listener.FileMonitorListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.devtools.filewatch.FileSystemWatcher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.time.Duration;

@Configuration
@Slf4j
public class FileWatcherConfig {

  @Bean
  public FileSystemWatcher fileSystemWatcher(ApplicationArguments args) {
    FileSystemWatcher fileSystemWatcher =
        new FileSystemWatcher(false, Duration.ofMillis(2000L), Duration.ofMillis(1000L));

    String path =
        args.getNonOptionArgs().stream()
            .findAny()
            .orElseGet(() -> System.getProperty("user.home") + File.separator + "Imagens");

    File file = new File(path);

    fileSystemWatcher.addSourceDirectory(file);
    fileSystemWatcher.addListener(new FileMonitorListener());
    fileSystemWatcher.start();

    log.info("Started fileSystemWatcher on directory {}", file.getPath());
    return fileSystemWatcher;
  }
}
