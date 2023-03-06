package org.cinrc.UI;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.cinrc.parser.CCSParser;
import org.cinrc.process.ProcessContainer;
import org.cinrc.process.ProcessTemplate;
import org.cinrc.process.process.Process;

public class RCCS_FX extends Application {
  @Override
  public void start(Stage stage) throws Exception {
    String javaVersion = System.getProperty("java.version");
    String javafxVersion = System.getProperty("javafx.version");
    setStage(stage);
  }

  public void setStage(Stage stage) throws IOException {
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(getClass().getResource("/ui/main.fxml"));
    VBox pane = loader.load();
    Scene scene = new Scene(pane);
    stage.setScene(scene);
    stage.show();
  }



  public void main(String[] args) {
    launch();
  }
}
