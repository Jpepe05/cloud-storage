package com.ti5.cloudstorage.listener;

import com.ti5.cloudstorage.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.devtools.filewatch.ChangedFile;
import org.springframework.boot.devtools.filewatch.ChangedFiles;
import org.springframework.boot.devtools.filewatch.FileChangeListener;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@Slf4j
@RequiredArgsConstructor
public class FileMonitorListener implements FileChangeListener {

  private final FileService fileService;

  @Override
  public void onChange(Set<ChangedFiles> changeSet) {
    for (ChangedFiles cfiles : changeSet) {
      for (ChangedFile cfile : cfiles.getFiles()) {
        log.info("{}: File ({}) changed", cfile.getType(), cfile.getFile().getPath());
        fileService.processFile(cfile);
      }
    }
  }
}
