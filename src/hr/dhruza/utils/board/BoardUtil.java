package hr.dhruza.utils.board;

import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.Optional;

public class BoardUtil {

  private BoardUtil() {}

  public static HBox getPlayerContainer(Node node, int elementId) {
    VBox container = (VBox) node.lookup("#cell-" + elementId);
    return (HBox) container.getChildren().get(1);
  }

  public static Optional<Node> getPlayerBoardObject(Node playerContainer, int elementId ) {
    Node foundPlayer = playerContainer.lookup("#player-" + elementId);
    return Optional.ofNullable(foundPlayer);
  }
}
