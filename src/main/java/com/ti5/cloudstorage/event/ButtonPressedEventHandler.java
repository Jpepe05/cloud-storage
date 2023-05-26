package com.ti5.cloudstorage.event;

import com.ti5.cloudstorage.constant.Constants;
import com.ti5.cloudstorage.ui.DownloadButton;
import com.ti5.cloudstorage.ui.SyncButton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
@Slf4j
@RequiredArgsConstructor
public class ButtonPressedEventHandler {

  private final DownloadButton downloadButton;
  private final SyncButton syncButton;

  @EventListener
  public void handleButtonPressed(ButtonPressedEvent buttonPressedEvent) {

    String buttonText = buttonPressedEvent.getButtonText();

    if (!backGroundTaskRunning()) {
      switch (buttonText) {
        case Constants.DOWNLOAD -> downloadButtonPressedEvent();
        case Constants.SYNC -> syncButtonPressedEvent();
        case Constants.FOLDER_SELECTION -> folderSelectionButtonPressedEvent(buttonPressedEvent);
        case Constants.BUCKET_SELECTION -> bucketSelectionButtonPressedEvent(buttonPressedEvent);
        default -> log.error("Unknown Button pressed: {}", buttonPressedEvent);
      }
    }
  }

  private boolean backGroundTaskRunning() {
    return downloadButton.isRunning() || syncButton.isRunning();
  }

  private void downloadButtonPressedEvent() {
    log.info("Download Button pressed on Thread: {}", Thread.currentThread().getId());
    downloadButton.reset();
    downloadButton.start();
  }

  private void syncButtonPressedEvent() {
    log.info("Sync Button pressed on Thread: {}", Thread.currentThread().getId());
    syncButton.reset();
    syncButton.start();
  }

  private void folderSelectionButtonPressedEvent(ButtonPressedEvent buttonPressedEvent) {
    File fileSelected = buttonPressedEvent.getFolderSelected();
    log.info("Folder {} selected", fileSelected.getPath());
  }

  private void bucketSelectionButtonPressedEvent(ButtonPressedEvent buttonPressedEvent) {
    String bucketName = buttonPressedEvent.getBucketName();
    log.info("Bucket {} selected", bucketName);
  }
}
