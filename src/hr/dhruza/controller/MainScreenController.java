package hr.dhruza.controller;

import hr.dhruza.GameContext;
import hr.dhruza.model.fx.FxDie;
import hr.dhruza.model.Player;
import hr.dhruza.utils.board.BoardBuilder;
import hr.dhruza.utils.board.BoardUtil;
import hr.dhruza.utils.board.WingsAndBombsBoardBuilder;
import hr.dhruza.utils.constants.DieConstants;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainScreenController implements Initializable {

  private final FxDie die = new FxDie();
  private final GameContext gameContext = GameContext.getInstance();

  BoardBuilder boardBuilder;

  @FXML private Button btnRoll;

  @FXML private Text lbDie;

  @FXML private GridPane gpMainBoard;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    setupGame();
    setDieText();
  }

  @FXML
  void onRollClick() {
    die.roll();
    setDieText();
    handleDieValueChange();
  }

  private void setupGame() {
    boardBuilder = new WingsAndBombsBoardBuilder(gpMainBoard);
    int sizeRow = gpMainBoard.getRowConstraints().size();
    int sizeColumn = gpMainBoard.getColumnConstraints().size();
    boardBuilder.build(sizeRow, sizeColumn);
    gameContext.setActivePlayer(GameContext.getInstance().getPlayers().get(0));
  }

  private void setDieText() {
    lbDie.textProperty().set(die.getValue().toString());
  }

  private void handleDieValueChange() {
    Integer newDieValue = die.getValue();
    Player activePlayer = gameContext.getActivePlayer();
    movePlayerToNextPosition(newDieValue, activePlayer);
    checkIfPlayerCanRollAgain(newDieValue);
  }

  private void checkIfPlayerCanRollAgain(Integer newDieValue) {
    if (!newDieValue.equals(DieConstants.EXTRA_ROLL_VALUE)) {
      gameContext.nextPlayer();
    }
  }

  private void movePlayerToNextPosition(Integer newDieValue, Player activePlayer) {
    HBox currentPosition = BoardUtil.getPlayerContainer(gpMainBoard, activePlayer.getPosition());

    Optional<Node> activePlayerBoardObject =
        BoardUtil.getPlayerBoardObject(currentPosition, activePlayer.getId());

    currentPosition.getChildren().remove(activePlayerBoardObject.get());
    activePlayer.setPosition(activePlayer.getPosition() + newDieValue);

    BoardUtil.getPlayerContainer(gpMainBoard, activePlayer.getPosition())
        .getChildren()
        .add(activePlayerBoardObject.get());
  }
}
