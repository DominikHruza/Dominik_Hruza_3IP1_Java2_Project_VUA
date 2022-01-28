package hr.dhruza.controller;

import hr.dhruza.constants.ChatConstants;
import hr.dhruza.constants.FileConstants;
import hr.dhruza.model.GameContext;
import hr.dhruza.model.GameContextSerializable;
import hr.dhruza.model.Player;
import hr.dhruza.model.PlayerColor;
import hr.dhruza.rmi.ChatServer;
import hr.dhruza.utils.DomUtils;
import hr.dhruza.utils.MessageUtils;
import hr.dhruza.utils.ReflectionUtils;
import hr.dhruza.utils.board.BoardBuilder;
import hr.dhruza.utils.board.BoardUtil;
import hr.dhruza.utils.board.WingsAndBombsBoardBuilder;
import hr.dhruza.utils.constants.BoardConstants;
import hr.dhruza.utils.constants.DieConstants;
import hr.dhruza.viewmodel.DieViewModel;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.*;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

abstract public class MainScreenController implements Initializable {
    protected ChatServer chatServer;
    Integer controllerId;
    final DieViewModel die = new DieViewModel();
    final GameContext gameContext = GameContext.INSTANCE;
    protected ObservableList<Node> messages;

    BoardBuilder boardBuilder;

    @FXML
    protected Button btnRoll;

    @FXML protected Text lbDie;

    @FXML protected GridPane gpMainBoard;

    @FXML
    protected MenuItem miSave;

    @FXML
    protected MenuItem miDocumentation;

    @FXML
    protected ScrollPane spContainer;

    @FXML
    protected VBox vbMessages;

    @FXML
    protected TextField tfMessage;

    @FXML
    protected Button btnSend;

    abstract void handleDieValueChange();

    @FXML
    void saveDOM() {
        DomUtils.savePlayers(gameContext.getPlayers());
    }

    @FXML
    void loadDOM() {
        clearPlayersFromBoard();
        List<Player> players = DomUtils.loadPlayers();
        gameContext.setPlayers(players);
        setPlayerPositions();
    }

    @FXML
    void onRollClick() {
        die.roll();
        GameContext.INSTANCE.setDieValue(die.getValue());
        setDieText();
        handleDieValueChange();
        toggleRoll();
    }


    void toggleRoll() {
        Integer activeId = gameContext.getActivePlayer().getId();
        btnRoll.setDisable(controllerId != activeId);
        String btnText = controllerId != activeId ? "Player " + activeId + " rolls!" : "Roll";
        btnRoll.setText(btnText);
    }

    void setupNewGame() {
        setupBoard();
        setDieText();
        GameContext.INSTANCE.setActivePlayer(GameContext.INSTANCE.getPlayers().get(0));
    }

     void setupLoadedGame() {
        setLoadedGameContextFromFile();
        setupBoard();
        setDieText();
        clearPlayersFromBoard();
        setPlayerPositions();
        BoardUtil.getPlayerContainer(gpMainBoard, 1)
                .getChildren()
                .clear();
    }

    public void refresh(GameContextSerializable gcs){
        gcs.loadToGlobalState();
        setDieText();
        Player activePlayer = gameContext.getActivePlayer();
        Integer currentPos = BoardUtil.getPlayerCurrentCell(gpMainBoard, activePlayer.getId());
        Integer nextPos = activePlayer.getPosition();
        movePlayerToNextPosition(activePlayer, currentPos, nextPos);
        checkIfPlayerCanRollAgain(gameContext.getDieValue());
        toggleRoll();
    }

    void setPlayerPositions() {
        for (Player player : gameContext.getPlayers()) {
            loadPlayerToPosition(player);
        }
    }

    void setupBoard() {
        boardBuilder = new WingsAndBombsBoardBuilder(gpMainBoard);
        int sizeRow = gpMainBoard.getRowConstraints().size();
        int sizeColumn = gpMainBoard.getColumnConstraints().size();
        boardBuilder.build(sizeRow, sizeColumn);
    }

    private void setLoadedGameContextFromFile() {
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FileConstants.SERIALIZED_STATE_FILE))) {
            GameContextSerializable contextSerializable = (GameContextSerializable) ois.readObject();
            contextSerializable.loadToGlobalState();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    void setDieText() {
        lbDie.textProperty().set(GameContext.INSTANCE.getDieValue().toString());
    }

    void checkIfPlayerCanRollAgain(Integer newDieValue) {
        if (!newDieValue.equals(DieConstants.EXTRA_ROLL_VALUE)) {
            GameContext.INSTANCE.nextPlayer();
        }
    }

     void movePlayerToNextPosition(Integer newDieValue, Player activePlayer) {
        HBox currentPosition = BoardUtil.getPlayerContainer(gpMainBoard, activePlayer.getPosition());

        currentPosition.getId();
        Optional<Node> activePlayerBoardObject =
                BoardUtil.getPlayerBoardObject(currentPosition, activePlayer.getId());

        if (activePlayerBoardObject.isPresent()){
            currentPosition.getChildren().remove(activePlayerBoardObject.get());
        }

         updatePlayerPosition(newDieValue, activePlayer);

         BoardUtil.getPlayerContainer(gpMainBoard, activePlayer.getPosition())
                .getChildren()
                .add(activePlayerBoardObject.get());
    }


    void movePlayerToNextPosition(Player player, Integer current, Integer next) {
        HBox currentPosition = BoardUtil.getPlayerContainer(gpMainBoard, current);

        currentPosition.getId();
        Optional<Node> activePlayerBoardObject =
                BoardUtil.getPlayerBoardObject(currentPosition, player.getId());

        if (activePlayerBoardObject.isPresent()){
            currentPosition.getChildren().remove(activePlayerBoardObject.get());
        }

        player.setPosition(next);
        BoardUtil.getPlayerContainer(gpMainBoard, player.getPosition())
                .getChildren()
                .add(activePlayerBoardObject.get());
    }

    private void updatePlayerPosition(Integer newDieValue, Player activePlayer) {
        Integer newPos = activePlayer.getPosition() + newDieValue;
        if(BoardConstants.BOMBS.containsKey(newPos)){
            activePlayer.setPosition(BoardConstants.BOMBS.get(newPos));
            return;
        }

        if(BoardConstants.WINGS.containsKey(newPos)){
            activePlayer.setPosition(BoardConstants.WINGS.get(newPos));
            return;
        }

        if(newPos > 81){
            activePlayer.setPosition(81);
            return;
        }

        activePlayer.setPosition(newPos);
    }

    private void loadPlayerToPosition(Player player){
        Circle playerCircle = new Circle(9);
        PlayerColor playerColor = PlayerColor.fromId(player.getId()).get();
        playerCircle.fillProperty().set(playerColor.getColorValue());
        playerCircle.idProperty().set("player-" + player.getId());

        BoardUtil.getPlayerContainer(gpMainBoard, player.getPosition())
                .getChildren()
                .add(playerCircle);
    }

    private void clearPlayersFromBoard(){
        for (Player player : gameContext.getPlayers()) {
            BoardUtil.getPlayerContainer(gpMainBoard, player.getPosition())
                    .getChildren()
                    .clear();
        }
    }

    @FXML
    void onSaveGame() {
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FileConstants.SERIALIZED_STATE_FILE))) {
            oos.writeObject(new GameContextSerializable());
            MessageUtils.showInfoMessage("Save", "Game saved", "Game saved successfully!");
        } catch (IOException  e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onLoadGame() {
        setupLoadedGame();
    }

    @FXML
    void createDocumentation() {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(FileConstants.DOCUMENTATION_FILENAME))) {
            DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(FileConstants.CLASSES_PATH));

            StringBuilder classAndMembersInfo = new StringBuilder();
            classAndMembersInfo
                    .append("<h1> Java 2 Game Project Documentation</h1>")
                    .append(System.lineSeparator());
            stream.forEach(file -> {
                String filename = file.getFileName().toString();
                String className = filename.substring(0, filename.indexOf("."));

                classAndMembersInfo
                        .append("<h3>" + className + "</h3>")
                        .append(System.lineSeparator());

                try {
                    Class<?> clazz = Class.forName(FileConstants.CLASSES_PACKAGE.concat(className));
                    ReflectionUtils.readClassAndMembersInfo(clazz, classAndMembersInfo);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(MainScreenController.class.getName()).log(Level.SEVERE, null, ex);
                }

                classAndMembersInfo
                        .append(System.lineSeparator())
                        .append(System.lineSeparator());
            });

            writer.write(classAndMembersInfo.toString());

            MessageUtils.showInfoMessage("Reflection", "Documentation created", "Check '" + FileConstants.DOCUMENTATION_FILENAME + "'");
        } catch (IOException ex) {
            Logger.getLogger(MainScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void send(KeyEvent event){
        if (event.getCode() == KeyCode.ENTER) {
            sendMessage();
        }
    }

    @FXML
    void sendBtnHandler(){
            sendMessage();
    }

    protected void sendMessage() {
        if (tfMessage.getText().trim().length() > 0) {
            chatServer.sendMessage(tfMessage.getText().trim());
            addMessage(tfMessage.getText().trim(), "Player " + controllerId , Color.BLACK);
            tfMessage.clear();
        }
    }

    protected void addMessage(String message, String name, Color color) {
        Label label = new Label();
        label.setFont(new Font(ChatConstants.FONT_SIZE));
        label.setTextFill(color);
        label.setText(String.format(
                ChatConstants.MESSAGE_FORMAT,
                LocalTime.now().format(DateTimeFormatter.ofPattern(ChatConstants.TIME_FORMAT)),
                name,
                message));

        messages.add(label);
        moveScrollPane();
    }

    private void moveScrollPane() {
        spContainer.applyCss();
        spContainer.layout();
        spContainer.setVvalue(1D);
    }

    public void postMessage(String message, String name, Color color) {
        Platform.runLater(() -> addMessage(message, name, color));
    }

    protected void checkGameWon() {
        GameContext.INSTANCE.getPlayers().forEach( player -> {
            if(player.getPosition() == 81){
                btnRoll.setDisable(true);
                btnRoll.setText(player.getName() + " won!");
            }
        });
    }
}
