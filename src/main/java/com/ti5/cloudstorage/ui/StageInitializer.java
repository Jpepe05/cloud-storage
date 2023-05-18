package com.ti5.cloudstorage.ui;

import com.ti5.cloudstorage.UIApplication;
import javafx.stage.Stage;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class StageInitializer implements ApplicationListener<UIApplication.StageReadyEvent> {

  @Override
  public void onApplicationEvent(UIApplication.StageReadyEvent event) {
    Stage stage = event.getStage();
    stage.show();
  }
}
