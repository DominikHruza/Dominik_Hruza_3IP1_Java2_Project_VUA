package hr.dhruza.model.fx;

import hr.dhruza.model.Player;
import hr.dhruza.model.PlayerColor;
import javafx.beans.property.*;

public class FxPlayer {
  private final Player player;

  public FxPlayer(Player player) {
    this.player = player;
  }

  public int getId() {
    return player.getId();
  }

  public IntegerProperty idProperty() {
    return new SimpleIntegerProperty(player.getId());
  }

  public String getName() {
    return player.getName();
  }

  public StringProperty nameProperty() {
    return new SimpleStringProperty(player.getName());
  }

  public int getPosition() {
    return player.getPosition();
  }

  public IntegerProperty positionProperty() {
    return new SimpleIntegerProperty(player.getPosition());
  }

  public PlayerColor getColor() {
    return player.getColor();
  }

  public ObjectProperty<PlayerColor> colorProperty() {
    return new SimpleObjectProperty<>(player.getColor());
  }

  public Player getPlayer() {
    return player;
  }
}
