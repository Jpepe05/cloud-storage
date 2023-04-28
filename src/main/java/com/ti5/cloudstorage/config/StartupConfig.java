package com.ti5.cloudstorage.config;

import com.ti5.cloudstorage.service.BackupService;
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

  private final BackupService backupService;
  private final FileSystemWatcher fileSystemWatcher;
  private final Set<File> watchedDirectories;

  @EventListener(ApplicationReadyEvent.class)
  public void syncFilesAndStartFileWatcher() {
    log.info("Syncing files");
    backupService.syncOnStartup();
    log.info("Finished syncing files, starting file watcher");
    fileSystemWatcher.start();
    log.info("Started fileSystemWatcher on directories {}", watchedDirectories);
  }
}
