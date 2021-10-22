package hr.dhruza.model;

import javafx.scene.paint.Color;

import java.util.Optional;

public enum PlayerColor {
  RED(Color.RED, 1),
  GREEN(Color.GREEN, 2),
  BLUE(Color.BLUE, 3),
  YELLOW(Color.GOLD, 4);

  private final Color colorValue;
  private final int id;

  PlayerColor(Color colorValue, int id) {
    this.colorValue = colorValue;
    this.id = id;
  }

  public int getId() {
    return id;
  }

  public Color getColorValue() {
    return colorValue;
  }

  public static Optional<PlayerColor> fromId(int inputId) {
    PlayerColor found = null;
    for (PlayerColor value : values()) {
      if (value.id == inputId) {
        found = value;
      }
    }
    return Optional.ofNullable(found);
  }
}
