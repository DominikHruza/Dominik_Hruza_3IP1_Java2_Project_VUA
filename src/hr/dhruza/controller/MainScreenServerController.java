package hr.dhruza.controller;

import hr.dhruza.model.GameContext;
import hr.dhruza.model.GameContextSerializable;
import hr.dhruza.model.Player;
import hr.dhruza.networking.tcp.Server;

import java.net.URL;
import java.util.ResourceBundle;

public class MainScreenServerController extends MainScreenController {

    private Server tcpServer;

    public MainScreenServerController() {
      controllerId = 1;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initServerTCPThread();
        setupNewGame();
        toggleRoll();
    }

    @Override
    void handleDieValueChange() {
        Player activePlayer = GameContext.INSTANCE.getActivePlayer();
        movePlayerToNextPosition(gameContext.getDieValue(), activePlayer);
        tcpServer.sendData(new GameContextSerializable());
        checkIfPlayerCanRollAgain(gameContext.getDieValue());
    }

    private void initServerTCPThread() {
        tcpServer = new Server(this);
        tcpServer.setDaemon(true);
        tcpServer.start();
    }
}
