package com.ti5.cloudstorage.ui;

import com.ti5.cloudstorage.config.UserConfigs;
import com.ti5.cloudstorage.service.BackupService;
import com.ti5.cloudstorage.watcher.FileSystemWatcherCustom;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.devtools.filewatch.FileSystemWatcher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DirectorySelectionButton extends Service<Void> {

  private final FileSystemWatcher fileSystemWatcher;
  private final UserConfigs userConfigs;

  @Override
  protected Task<Void> createTask() {
    return new DownloadTask(fileSystemWatcher, userConfigs);
  }

  @RequiredArgsConstructor
  private static class DownloadTask extends Task<Void> {

    private final FileSystemWatcher fileSystemWatcher;
    private final UserConfigs userConfigs;

    @Override
    protected Void call() {
      log.info("Changing folder on thread {}", Thread.currentThread().getId());
      ((FileSystemWatcherCustom) fileSystemWatcher)
          .changeFolder(userConfigs.getRootDirectoryPath().toFile());
      return null;
    }
  }
}
