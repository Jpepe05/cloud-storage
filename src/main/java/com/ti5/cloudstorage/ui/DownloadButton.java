package com.ti5.cloudstorage.ui;

import com.ti5.cloudstorage.service.BackupService;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.devtools.filewatch.FileSystemWatcher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DownloadButton extends Service<Void> {

  private final BackupService backupService;
  private final FileSystemWatcher fileSystemWatcher;

  @Override
  protected Task<Void> createTask() {
    return new DownloadTask(backupService, fileSystemWatcher);
  }

  @RequiredArgsConstructor
  private static class DownloadTask extends Task<Void> {

    private final BackupService backupService;
    private final FileSystemWatcher fileSystemWatcher;

    @Override
    protected Void call() {
      log.info("Downloading started on Thread: {}", Thread.currentThread().getId());
      fileSystemWatcher.stop();
      backupService.download();
      fileSystemWatcher.start();
      return null;
    }
  }
}
