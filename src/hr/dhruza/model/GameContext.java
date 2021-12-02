package hr.dhruza.model;

import java.util.ArrayList;
import java.util.List;

public enum GameContext {

  INSTANCE();

  private PlayerCount playerCount = PlayerCount.TWO;
  private List<Player> players = new ArrayList<>();
  private Player activePlayer;
  private int dieValue;

  public void nextPlayer(){
    int activeIndex = players.indexOf(this.activePlayer);
    if(activeIndex != players.size() - 1){
      activeIndex++;
      activePlayer = players.get(activeIndex);
      return;
    }
    activePlayer = players.get(0);
  }

  public void setPlayerCount(PlayerCount playerCount) {
    this.playerCount = playerCount;
  }

  public PlayerCount getPlayerCount() {
    return playerCount;
  }

  public List<Player> getPlayers() {
    return players;
  }

  public void setPlayers(List<Player> players){
    this.players = players;
  }
  public void addPlayer(Player player) {
    this.players.add(player);
  }

  public Player getActivePlayer() {
    return activePlayer;
  }

  public void setActivePlayer(Player activePlayer) {
    this.activePlayer = activePlayer;
  }

  public int getDieValue() {
    return dieValue;
  }

  public void setDieValue(int dieValue) {
    this.dieValue = dieValue;
  }
}
