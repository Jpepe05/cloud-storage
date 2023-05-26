package com.ti5.cloudstorage.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.devtools.filewatch.FileSystemWatcher;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

import java.io.File;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class StartupConfig {

  private final FileSystemWatcher fileSystemWatcher;
  private final Set<File> watchedDirectories;

  @EventListener(ApplicationReadyEvent.class)
  public void startFileWatcher() {
    log.info("Starting file watcher");
    fileSystemWatcher.start();
    log.info("Started fileSystemWatcher on directories {}", watchedDirectories);
  }
}
