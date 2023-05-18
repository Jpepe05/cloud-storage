package com.ti5.cloudstorage.ui;

import com.ti5.cloudstorage.UIApplication;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class StageInitializer implements ApplicationListener<UIApplication.StageReadyEvent> {

  @Value("classpath:/ui.fxml")
  private Resource uiResource;

  @Value("${spring.application.ui.title}")
  private String applicationTitle;

  private final ApplicationContext applicationContext;

  @Override
  public void onApplicationEvent(UIApplication.StageReadyEvent event) {
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(uiResource.getURL());
      fxmlLoader.setControllerFactory(applicationContext::getBean);
      Parent parent = fxmlLoader.load();

      Stage stage = event.getStage();
      stage.setTitle(applicationTitle);
      stage.setScene(new Scene(parent, 800, 600));
      stage.show();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
