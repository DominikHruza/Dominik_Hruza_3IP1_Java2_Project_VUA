package hr.dhruza.model;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.List;

public class GameContextSerializable implements Externalizable {
    private boolean init;
    private final static Long  serialVersionUID = 2L;
    private PlayerCount playerCount = PlayerCount.TWO;
    private List<Player> players = new ArrayList<>();
    private Player activePlayer;
    private int dieValue;

    public GameContextSerializable() {
        this.playerCount = GameContext.INSTANCE.getPlayerCount();
        this.players = GameContext.INSTANCE.getPlayers();
        this.activePlayer = GameContext.INSTANCE.getActivePlayer();
        this.dieValue = GameContext.INSTANCE.getDieValue();
    }

    public void loadToGlobalState(){
        GameContext.INSTANCE.setPlayerCount(this.playerCount);
        GameContext.INSTANCE.setPlayers(this.players);
        GameContext.INSTANCE.setActivePlayer(this.activePlayer);
        GameContext.INSTANCE.setDieValue( this.dieValue );
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(playerCount);
        out.writeObject(players);
        out.writeObject(activePlayer);
        out.writeInt(dieValue);

    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
       this.playerCount = (PlayerCount) in.readObject();
       this.players = (ArrayList<Player>) in.readObject();
       this.activePlayer = (Player) in.readObject();
       this.dieValue = in.readInt();
    }

    public boolean isInit() {
        return init;
    }

    public void setInit(boolean init) {
        this.init = init;
    }
}
