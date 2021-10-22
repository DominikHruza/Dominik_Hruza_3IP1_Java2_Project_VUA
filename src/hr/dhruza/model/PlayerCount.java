package hr.dhruza.model;

import java.util.Optional;

public enum PlayerCount {
  TWO("2 players", 2),
  THREE("3 players", 3),
  FOUR("4 players", 4);

  private final String name;
  private final int count;

  PlayerCount(String value, int count) {
    this.name = value;
    this.count = count;
  }

  public int getCount() {
    return count;
  }

  public String getName() {
    return name;
  }

  public static Optional<PlayerCount> fromName(String input) {
    PlayerCount found = null;
    for (PlayerCount value : values()) {
      if (value.name.equals(input)) {
        found = value;
      }
    }
    return Optional.ofNullable(found);
  }
}
