package com.ti5.cloudstorage.watcher;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.devtools.filewatch.FileSystemWatcher;

import java.io.File;
import java.lang.reflect.Field;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class FileSystemWatcherCustom extends FileSystemWatcher {

  public FileSystemWatcherCustom(boolean daemon, Duration pollInterval, Duration quietPeriod) {
    super(daemon, pollInterval, quietPeriod, null);
  }

  @Override
  public void start() {
    try {
      Field field = getClass().getSuperclass().getDeclaredField("remainingScans");
      field.setAccessible(true);
      AtomicInteger atomicInteger = (AtomicInteger) field.get(this);
      atomicInteger.set(-1);
      field.setAccessible(false);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      log.error("Error setting remainingScans to -1", e);
    }
    super.start();
    log.info("File System Watcher started");
  }

  @Override
  public void stop() {
    super.stop();
    log.info("File System Watcher stopped");
  }

  public void changeFolder(File directory) {
    this.stop();
    removeOldDirectory();
    super.addSourceDirectory(directory);
    this.start();
  }

  private void removeOldDirectory() {
    try {
      Field field = getClass().getSuperclass().getDeclaredField("directories");
      field.setAccessible(true);
      Map directories = (Map) field.get(this);
      directories.remove(directories.keySet().iterator().next());
      field.setAccessible(false);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      log.error("Error setting remainingScans to -1", e);
    }
  }
}
