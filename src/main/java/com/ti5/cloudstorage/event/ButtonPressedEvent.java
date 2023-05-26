package com.ti5.cloudstorage.event;

import lombok.Getter;

@Getter
public class ButtonPressedEvent {

  private final String buttonText;

  public ButtonPressedEvent(String buttonText) {
    this.buttonText = buttonText;
  }
}
