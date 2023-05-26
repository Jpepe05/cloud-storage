package com.ti5.cloudstorage;

import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class CloudStorageApplication {

  public static void main(String[] args) {
    Application.launch(UIApplication.class, args);
  }
}
