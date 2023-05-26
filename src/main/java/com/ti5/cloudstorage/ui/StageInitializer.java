package com.ti5.cloudstorage.ui;

import com.ti5.cloudstorage.UIApplication;
import com.ti5.cloudstorage.constant.Constants;
import com.ti5.cloudstorage.event.ButtonPressedEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class StageInitializer {

  @Value("${spring.application.ui.title}")
  private String applicationTitle;

  private final ApplicationContext applicationContext;
  @EventListener
  public void onApplicationEvent(UIApplication.StageReadyEvent event) {
    try {
      VBox vBox = new VBox();

      HBox topRightBox = new HBox(10);
      topRightBox.setAlignment(Pos.TOP_RIGHT);
      topRightBox.setPadding(new Insets(10, 10, 10, 10));
      topRightBox
          .getChildren()
          .addAll(
              createButton(Constants.FOLDER_SELECTION, "green"),
              createButton(Constants.BUCKET_SELECTION, "purple"));

      HBox logo = new HBox();
      logo.setPadding(new Insets(25, 25, 25, 25));
      Rectangle rectangule = new Rectangle();
      rectangule.setFill(Color.YELLOW);
      rectangule.setHeight(75);

      HBox bottomLeftBox = new HBox(10);
      bottomLeftBox.setAlignment(Pos.BOTTOM_LEFT);
      bottomLeftBox.setPadding(new Insets(10, 10, 10, 10));
      bottomLeftBox
          .getChildren()
          .addAll(createButton(Constants.DOWNLOAD, "red"), createButton(Constants.SYNC, "blue"));

      vBox.getChildren().addAll(topRightBox, rectangule, bottomLeftBox);

      Stage stage = event.getStage();
      stage.setTitle(applicationTitle);
      Scene scene = new Scene(vBox, 500, 200);
      rectangule.widthProperty().bind(scene.widthProperty());
      stage.setScene(scene);
      stage.show();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private Button createButton(String text, String colour) {
    Button btn = new Button(text);
    btn.setStyle(
        String.format(
            "-fx-background-radius: 10px; -fx-border-radius: 10px;-fx-border-color: %s;", colour));
    btn.setOnAction(
        e -> {
          log.info("Button pressed on thread " + Thread.currentThread().getId());
          applicationContext.publishEvent(new ButtonPressedEvent(btn.getText()));
        });
    return btn;
  }
}
