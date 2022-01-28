package hr.dhruza.controller;

import hr.dhruza.constants.ChatConstants;
import hr.dhruza.model.GameContext;
import hr.dhruza.model.GameContextSerializable;
import hr.dhruza.model.Player;
import hr.dhruza.networking.tcp.Server;
import hr.dhruza.rmi.ChatServer;
import javafx.beans.binding.Bindings;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;

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
        chatServer = new ChatServer(this);

        messages =  FXCollections.observableArrayList();
        Bindings.bindContentBidirectional(messages, vbMessages.getChildren());
        tfMessage.textProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue.length() >= ChatConstants.MESSAGE_LENGTH) {
                        ((StringProperty) observable).setValue(oldValue);
                    }
                }
        );
    }

    @Override
    void handleDieValueChange() {
        Player activePlayer = GameContext.INSTANCE.getActivePlayer();
        movePlayerToNextPosition(gameContext.getDieValue(), activePlayer);
        tcpServer.sendData(new GameContextSerializable());
        checkIfPlayerCanRollAgain(gameContext.getDieValue());
        checkGameWon();
    }


    private void initServerTCPThread() {
        tcpServer = new Server(this);
        tcpServer.setDaemon(true);
        tcpServer.start();
    }
}
