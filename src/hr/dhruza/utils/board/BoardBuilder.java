package hr.dhruza.utils.board;

import hr.dhruza.GameContext;
import hr.dhruza.model.Player;
import hr.dhruza.model.PlayerColor;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;

public abstract class BoardBuilder {

  GridPane board;

  public BoardBuilder(GridPane gp) {
    board = gp;
  }

  public final void build(int rowCount, int colCount) {
    setUpBoardFields(rowCount, colCount);
    setupPlayers(BoardUtil.getPlayerContainer(board, 1));
  }

  private void setUpBoardFields(int rowCount, int colCount) {
    int cellNum = 1;
    for (int i = rowCount - 1; i >= 0; i--) {
      for (int j = 0; j < colCount; j++) {
        VBox cellContainer = createCellContainer(cellNum);
        setCellLabel(cellContainer, cellNum);
        HBox playerIconHBox = new HBox();
        cellContainer.getChildren().add(playerIconHBox);

        board.add(cellContainer, j, i);
        cellNum++;
      }
    }
  }

  abstract void setCellLabel(VBox cell, int cellNum);

  protected Label createCellLabel(int cellNum) {
    Label fieldNumLabel = new Label();
    fieldNumLabel.fontProperty().setValue(new Font(18));
    fieldNumLabel.textProperty().set(Integer.toString(cellNum));
    return fieldNumLabel;
  }

  private VBox createCellContainer(int cellNum) {
    VBox vBox = new VBox();
    vBox.idProperty().set("cell-" + cellNum);
    vBox.paddingProperty().set(new Insets(3));
    return vBox;
  }

  public void setupPlayers(HBox playerContainer) {
    int numOfPlayersInGame = GameContext.getInstance().getPlayerCount().getCount();
    for (int i = 1; i <= numOfPlayersInGame; i++) {
      Circle player = new Circle(9);
      PlayerColor playerColor = PlayerColor.fromId(i).get();
      player.fillProperty().set(playerColor.getColorValue());
      player.idProperty().set("player-" + i);
      playerContainer.getChildren().add(player);
      GameContext.getInstance().addPlayer(new Player(i, "Player " + i, 1, playerColor));
    }
  }
}
