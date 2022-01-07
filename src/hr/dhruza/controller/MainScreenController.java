package hr.dhruza.controller;

import hr.dhruza.constants.FileConstants;
import hr.dhruza.model.GameContext;
import hr.dhruza.model.GameContextSerializable;
import hr.dhruza.model.Player;
import hr.dhruza.model.PlayerColor;
import hr.dhruza.utils.MessageUtils;
import hr.dhruza.utils.ReflectionUtils;
import hr.dhruza.utils.board.BoardBuilder;
import hr.dhruza.utils.board.BoardUtil;
import hr.dhruza.utils.board.WingsAndBombsBoardBuilder;
import hr.dhruza.utils.constants.DieConstants;
import hr.dhruza.viewmodel.DieViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import java.io.*;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

abstract public class MainScreenController implements Initializable {

    Integer controllerId;
    final DieViewModel die = new DieViewModel();
    final GameContext gameContext = GameContext.INSTANCE;

    BoardBuilder boardBuilder;

    @FXML
    private Button btnRoll;

    @FXML private Text lbDie;

    @FXML private GridPane gpMainBoard;

    @FXML
    private MenuItem miSave;

    @FXML
    private MenuItem miDocumentation;

    abstract void handleDieValueChange();

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
        setPlayerPositions();
        BoardUtil.getPlayerContainer(gpMainBoard, 1)
                .getChildren()
                .clear();
    }

    public void refresh(GameContextSerializable gcs){
        gcs.loadToGlobalState();
        setDieText();
        Player activePlayer = gameContext.getActivePlayer();
        Integer currentPos = activePlayer.getPosition() - gameContext.getDieValue();
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

        activePlayer.setPosition(activePlayer.getPosition() + newDieValue);

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

    private void loadPlayerToPosition(Player player){
        Circle playerCircle = new Circle(9);
        PlayerColor playerColor = PlayerColor.fromId(player.getId()).get();
        playerCircle.fillProperty().set(playerColor.getColorValue());
        playerCircle.idProperty().set("player-" + player.getId());

        BoardUtil.getPlayerContainer(gpMainBoard, player.getPosition())
                .getChildren()
                .add(playerCircle);
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
}
