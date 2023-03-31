package com.ti5.cloudstorage.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.devtools.filewatch.ChangedFile;
import org.springframework.boot.devtools.filewatch.ChangedFiles;
import org.springframework.boot.devtools.filewatch.FileChangeListener;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@Slf4j
public class FileMonitorListener implements FileChangeListener {

  @Override
  public void onChange(Set<ChangedFiles> changeSet) {
    for (ChangedFiles cfiles : changeSet) {
      for (ChangedFile cfile : cfiles.getFiles()) {
        log.info(cfile.getType().toString() + ":" + cfile.getFile().getName());
      }
    }
  }
}
