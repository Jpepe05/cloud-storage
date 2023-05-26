package com.ti5.cloudstorage.event;

import lombok.Getter;

import java.io.File;

@Getter
public class ButtonPressedEvent {

  private final String buttonText;
  private File folderSelected;
  private String bucketName;

  public ButtonPressedEvent(String buttonText) {
    this.buttonText = buttonText;
  }

  public ButtonPressedEvent(String buttonText, File folderSelected) {
    this.buttonText = buttonText;
    this.folderSelected = folderSelected;
  }

  public ButtonPressedEvent(String buttonText, String bucketName) {
    this.buttonText = buttonText;
    this.bucketName = bucketName;
  }
}
