package dungeon.gui;

import dungeon.engine.GameCell;
import dungeon.engine.GameEngine;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import java.io.*;

public class Controller {

    @FXML private GridPane gridPane;
    @FXML private Button btnUp;
    @FXML private Button btnDown;
    @FXML private Button btnLeft;
    @FXML private Button btnRight;
    @FXML private Button btnSave;
    @FXML private Button btnLoad;
    @FXML private Button btnRunGame;
    @FXML private TextField txtDifficulty;
    @FXML private TextField txtName;
    @FXML private Label lblHP;
    @FXML private Label lblScore;
    @FXML private Label lblSteps;
    @FXML private Label lblLevel;
    @FXML private Label lblPlayerName;

    private GameEngine engine;

    /**
     * Initializes the GUI layout and grid visuals,
     * but does not start the game until 'Run Game' is pressed.
     */
    @FXML
    public void initialize() {
        gridPane.setMinSize(640, 640);
        gridPane.setMaxSize(640, 640);
        gridPane.setPrefSize(640, 640);
        gridPane.setHgap(0);
        gridPane.setVgap(0);
        gridPane.setPadding(Insets.EMPTY);
        setGridBackground();
    }

    /**
     * Starts the game using difficulty entered in the text field.
     */
    @FXML
    private void handleRunGame(ActionEvent event) {
        System.out.println("txtName = " + txtName); // Check if null

        int difficulty = 3;
        try {
            difficulty = Integer.parseInt(txtDifficulty.getText());
            if (difficulty < 0 || difficulty > 10) difficulty = 3;
        } catch (NumberFormatException e) {
            System.out.println("Invalid difficulty entered. Using default of 3.");
        }

        String playerName = txtName.getText().isEmpty() ? "Unknown" : txtName.getText();
        engine = new GameEngine(10, difficulty, playerName); // Calls constructor with difficulty and name
        updateGui();
        updateStats();
        lblLevel.setText("1");
        lblPlayerName.setText(playerName);
    }

    @FXML private void handleUp(ActionEvent event) { processMove("up"); }
    @FXML private void handleDown(ActionEvent event) { processMove("down"); }
    @FXML private void handleLeft(ActionEvent event) { processMove("left"); }
    @FXML private void handleRight(ActionEvent event) { processMove("right"); }

    @FXML
    private void handleSave(ActionEvent event) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("savegame.dat"))) {
            out.writeObject(engine);
            System.out.println("✅ Game saved.");
        } catch (IOException e) {
            System.err.println("Save failed: " + e.getMessage());
        }
    }

    @FXML
    private void handleLoad(ActionEvent event) {
        File saveFile = new File("savegame.dat");

        if (!saveFile.exists()) {
            System.err.println("No save file found.");
            return;
        }

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(saveFile))) {
            engine = (GameEngine) in.readObject();
            updateGui();
            updateStats();
            lblLevel.setText(String.valueOf(engine.getCurrentLevel()));
            lblPlayerName.setText("Adventurer");
            System.out.println("Game loaded. Player at (" +
                    engine.getPlayer().getRow() + ", " +
                    engine.getPlayer().getCol() + ")");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Load failed: " + e.getMessage());
        }
    }

    private void processMove(String direction) {
        if (engine == null) return;

        try {
            boolean moved = engine.movePlayer(direction);
            if (moved) {
                GameCell cell = engine.getMap()[engine.getPlayer().getRow()][engine.getPlayer().getCol()];
                if (cell != null) {
                    System.out.println(cell.onEnter(engine.getPlayer()));
                } else {
                    System.out.println("⚠ Player moved into a null cell!");
                }
            }
            updateGui();
            updateStats();
        } catch (Exception e) {
            System.err.println("Error during movement: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void updateGui() {
        gridPane.getChildren().clear();
        for (int i = 0; i < engine.getSize(); i++) {
            for (int j = 0; j < engine.getSize(); j++) {
                GameCell cell = engine.getMap()[i][j];
                boolean hasPlayer = (engine.getPlayer().getRow() == i && engine.getPlayer().getCol() == j);
                GameCellView cellView = new GameCellView(cell, hasPlayer);
                gridPane.add(cellView, j, i);
            }
        }
        gridPane.setGridLinesVisible(true);
    }

    private void updateStats() {
        lblHP.setText("HP: " + engine.getPlayer().getHealth());
        lblScore.setText("Score: " + engine.getPlayer().getScore());
        lblSteps.setText("Steps Left: " + engine.getPlayer().getStepsLeft());
    }

    private void setGridBackground() {
        Image bgImage = new Image(getClass().getResource("/images/background.png").toExternalForm());
        BackgroundImage bg = new BackgroundImage(
                bgImage,
                BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT,
                BackgroundPosition.DEFAULT,
                new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, false)
        );
        gridPane.setBackground(new Background(bg));
    }
}