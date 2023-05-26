package com.ti5.cloudstorage.ui;

import com.ti5.cloudstorage.service.BackupService;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SyncButton extends Service<Void> {

  private final BackupService backupService;

  @Override
  protected Task<Void> createTask() {
    return new SyncTask(backupService);
  }

  @RequiredArgsConstructor
  private static class SyncTask extends Task<Void> {

    private final BackupService backupService;

    @Override
    protected Void call() {
      log.info("Syncing started on Thread: {}", Thread.currentThread().getId());
      backupService.sync();
      return null;
    }
  }
}
