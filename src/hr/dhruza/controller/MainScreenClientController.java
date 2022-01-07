package hr.dhruza.controller;

import hr.dhruza.model.GameContext;
import hr.dhruza.model.GameContextSerializable;
import hr.dhruza.model.Player;

import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class MainScreenClientController extends MainScreenController {

    private hr.dhruza.networking.tcp.Client client;
    private Socket tcpClientSocket;

    public MainScreenClientController() {
        controllerId = 2;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initClientTcpThread();
        setupNewGame();
        toggleRoll();
    }

    @Override
    void handleDieValueChange() {
        Player activePlayer = GameContext.INSTANCE.getActivePlayer();
        movePlayerToNextPosition(gameContext.getDieValue(), activePlayer);
        client.sendData(new GameContextSerializable());
        checkIfPlayerCanRollAgain(gameContext.getDieValue());
    }

    private void initClientTcpThread() {
        client = new hr.dhruza.networking.tcp.Client(this);
        client.setDaemon(true);
        client.start();
    }
}
