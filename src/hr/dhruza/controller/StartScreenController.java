package hr.dhruza.controller;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class StartScreenController implements Initializable {

  @FXML private Button btnStart;

  @FXML private Button btnLoad;

  @FXML
  void onStartButtonClick(Event event) {
    try {
      redirectToMainScreen(event);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @FXML
  void onJoinButtonClick(Event event) {
    try {
      redirectToMainScreen(event);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


  private void redirectToMainScreen(Event event) throws IOException {

    Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    currentStage.close();

    FXMLLoader loader = new FXMLLoader(getClass().getResource("/hr/dhruza/view/MainScreen.fxml"));

    Button targetBtn = (Button) event.getTarget();
    if(targetBtn.getId().equals("btnStart")){
      loader.setController(new MainScreenServerController());
    } else {
      loader.setController(new MainScreenClientController());
    }

    Parent root = null;
    root = loader.load();

    Scene scene = new Scene(root);
    Stage stage = new Stage();

    stage.setTitle("Play!");
    stage.setScene(scene);
    stage.show();
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {

  }
}
