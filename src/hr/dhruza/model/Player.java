package hr.dhruza.model;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class Player implements Externalizable {
  private final static long serialVersionUID = 1L;

  private Integer id;
  private String name;
  private Integer position;
  private PlayerColor color;

  public Player() {}

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

  @Override
  public void writeExternal(ObjectOutput out) throws IOException {
    out.writeInt(id);
    out.writeUTF(name);
    out.writeInt(position);

  }

  @Override
  public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
    this.id = in.readInt();
    this.name = in.readUTF();
    this.position = in.readInt();
  }
}
