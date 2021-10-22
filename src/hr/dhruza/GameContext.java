package hr.dhruza;

import hr.dhruza.model.Player;
import hr.dhruza.model.PlayerCount;

import java.util.ArrayList;
import java.util.List;

public class GameContext {
  private static GameContext instance;
  private PlayerCount playerCount = PlayerCount.TWO;
  private final List<Player> players = new ArrayList<>();
  private Player activePlayer;

  private GameContext() {}

  public static GameContext getInstance() {
    if (instance == null) {
      instance = new GameContext();
    }
    return instance;
  }

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

  public void addPlayer(Player player) {
    this.players.add(player);
  }

  public Player getActivePlayer() {
    return activePlayer;
  }

  public void setActivePlayer(Player activePlayer) {
    this.activePlayer = activePlayer;
  }
}
