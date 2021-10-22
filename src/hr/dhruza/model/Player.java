package hr.dhruza.model;

public class Player {

  private final Integer id;
  private final String name;
  private Integer position;
  private final PlayerColor color;

  public Player(Integer id, String name, Integer position, PlayerColor color) {
    this.id = id;
    this.name = name;
    this.position = position;
    this.color = color;
  }

  public Integer getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public Integer getPosition() {
    return position;
  }

  public void setPosition(Integer position) {
    this.position = position;
  }

  public PlayerColor getColor() {
    return color;
  }
}
