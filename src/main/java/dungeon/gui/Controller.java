package dungeon.gui;

import dungeon.engine.GameCell;
import dungeon.engine.GameEngine;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import java.io.*;

public class Controller {

    @FXML private GridPane gridPane;
    @FXML private Button btnUp;
    @FXML private Button btnDown;
    @FXML private Button btnHelp;
    @FXML private Button btnLeft;
    @FXML private Button btnRight;
    @FXML private Button btnSave;
    @FXML private Button btnLoad;
    @FXML private Button btnRunGame;
    @FXML private TextArea txtTopScores;
    @FXML private TextField txtDifficulty;
    @FXML private TextField txtName;
    @FXML private Label lblHP;
    @FXML private Label lblScore;
    @FXML private Label lblSteps;
    @FXML private Label lblLevel;
    @FXML private Label lblMessage;
    @FXML private Label lblPlayerName;

    /**
     * Creates a Help Button to provide game information
     */
    @FXML
    private void handleHelp(ActionEvent event) {
        String helpText = """
        📖 MiniDungeon Help

        🎮 Objective:
        - Explore the dungeon, collect gold and potions, and survive mutant attacks!
        - Reach the ladder to advance to the next level.
        - The game ends when your HP hits 0 or you run out of steps.

        🎯 Controls:
        - Use the movement buttons (Up, Down, Left, Right) to move.
        - Save/Load your game at any time.
        
        🧪 Items:
        - 💰 Gold: +2 points
        - 🧪 Potion: +3 HP (up to max 10)
        - 🧟 Melee Mutant: causes damage
        - 🧙 Ranged Mutant: may damage from afar
        - 🪜 Ladder: go to next level (difficulty +2)

        🏆 Top Scores:
        - Finishing the game with a high score may earn a Top 5 spot!

        Good luck, adventurer!
        """;

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Help");
        alert.setHeaderText("MiniDungeon Instructions");
        alert.setContentText(helpText);
        alert.getDialogPane().setPrefWidth(450);
        alert.showAndWait();
    }

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

        int difficulty = 3;
        try {
            difficulty = Integer.parseInt(txtDifficulty.getText());
            if (difficulty < 0 || difficulty > 10) difficulty = 3;

        } catch (NumberFormatException e) {
            System.out.println("❗ Invalid difficulty, using default.");
        }


        String playerName = txtName.getText().isEmpty() ? "Unknown" : txtName.getText();
        System.out.println("✔ Player name: " + playerName);

        try {
            engine = new GameEngine(10, difficulty, playerName); // Can freeze here
            System.out.println("✔ GameEngine created");
        } catch (Exception e) {
            System.err.println("❌ Failed to create GameEngine: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        try {
            updateGui();
            updateStats();

            lblLevel.setText(String.valueOf(engine.getCurrentLevel()));
            lblPlayerName.setText(playerName);
            updateTopScores();
        } catch (Exception e) {
            System.err.println("❌ Error during GUI or stats update: " + e.getMessage());
            e.printStackTrace();
        }

        lblMessage.setText("Game started. Explore carefully!");
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
            updateTopScores();
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
                    String result = cell.onEnter(engine.getPlayer());
                    showMessage(result);  // Display result in lblMessage
                } else {
                    showMessage("Player moved into a mysterious void!");
                }
            }

            updateGui();
            updateStats();


            // Ensure Level label updates with current level
            lblLevel.setText(String.valueOf(engine.getCurrentLevel()));
            txtDifficulty.setText(String.valueOf(engine.getDifficulty()));

            if (engine.isGameOver()) {
                boolean topScoreAchieved = dungeon.engine.ScoreManager.addScore(engine.getPlayer().getName(), engine.getPlayer().getScore());
                Alert alert = new Alert(AlertType.INFORMATION);
                if (topScoreAchieved) {
                    alert.setTitle("🎉 New Top Score!");
                    alert.setHeaderText("Congratulations!");
                    alert.setContentText("You made the Top 5!\nScore: " + engine.getPlayer().getScore());
                } else {
                    alert.setTitle("Game Over");
                    alert.setHeaderText(null);
                    alert.setContentText("Thanks for playing!\nScore: " + engine.getPlayer().getScore());
                }
                alert.showAndWait();

                updateTopScores();
            }

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

    private void updateTopScores() {
        if (txtTopScores == null) return; // Failsafe
        StringBuilder sb = new StringBuilder();
        int rank = 1;
        for (var entry : dungeon.engine.ScoreManager.getTopScores()) {
            sb.append(rank).append(". ").append(entry.name).append(" - ").append(entry.score).append("\n");
            rank++;
        }
        txtTopScores.setText(sb.toString());
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

    private void showMessage(String message) {
        lblMessage.setText(message);
    }

}