package hr.dhruza.controller;

import hr.dhruza.model.GameContext;
import hr.dhruza.model.PlayerCount;
import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

public class StartScreenController implements Initializable {

  @FXML private Button btnStart;

  @FXML private Button btnLoad;

  @FXML private ComboBox<String> cbPlayers;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    intComboBox();
  }

  private void intComboBox() {

    cbPlayers.setItems(
        FXCollections.observableArrayList(
            Arrays.asList(
                PlayerCount.TWO.getName(),
                PlayerCount.THREE.getName(),
                PlayerCount.FOUR.getName())));

    cbPlayers.getSelectionModel().select(0);
  }

  @FXML
  void onStartButtonClick(Event event) {
    try {
      redirectToMainScreen(event);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @FXML
  void onPlayerCountSelectionChange() {
    PlayerCount playerCount =
        PlayerCount.fromName(cbPlayers.getSelectionModel().getSelectedItem()).get();
    GameContext.INSTANCE.setPlayerCount(playerCount);
  }

  private void redirectToMainScreen(Event event) throws IOException {

    Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    currentStage.close();

    FXMLLoader loader = new FXMLLoader(getClass().getResource("/hr/dhruza/view/MainScreen.fxml"));
    loader.setController(new MainScreenController(event));
    Parent root = loader.load();
    Scene scene = new Scene(root);
    Stage stage = new Stage();


    stage.setTitle("Play!");
    stage.setScene(scene);
    stage.show();
  }
}
