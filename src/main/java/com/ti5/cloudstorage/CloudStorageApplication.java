package com.ti5.cloudstorage;

import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CloudStorageApplication {

  public static void main(String[] args) {
    Application.launch(UIApplication.class, args);
  }
}
