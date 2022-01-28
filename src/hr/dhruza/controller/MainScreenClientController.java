package hr.dhruza.controller;

import hr.dhruza.constants.ChatConstants;
import hr.dhruza.model.GameContext;
import hr.dhruza.model.GameContextSerializable;
import hr.dhruza.model.Player;
import hr.dhruza.rmi.ChatClient;
import javafx.beans.binding.Bindings;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.paint.Color;

import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class MainScreenClientController extends MainScreenController {

    private ChatClient chatClient;
    private hr.dhruza.networking.tcp.Client client;
    private Socket tcpClientSocket;

    public MainScreenClientController() {
        controllerId = 2;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        initClientTcpThread();
        initChat();
        setupNewGame();
        toggleRoll();

    }

    private void initChat() {
        chatClient = new ChatClient(this);
        messages = FXCollections.observableArrayList();
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
        client.sendData(new GameContextSerializable());
        checkIfPlayerCanRollAgain(gameContext.getDieValue());
        checkGameWon();
    }


    @FXML
    @Override
    protected void sendMessage() {
        if (tfMessage.getText().trim().length() > 0) {
            chatClient.sendMessage(tfMessage.getText().trim());
            addMessage(tfMessage.getText().trim(), ChatConstants.CLIENT_NAME, Color.BLACK);
            tfMessage.clear();
        }
    }

    private void initClientTcpThread() {
        client = new hr.dhruza.networking.tcp.Client(this);
        client.setDaemon(true);
        client.start();
    }
}
