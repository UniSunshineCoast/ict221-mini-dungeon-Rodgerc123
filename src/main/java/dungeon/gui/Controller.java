package dungeon.gui;

import dungeon.engine.GameCell;
import dungeon.engine.GameEngine;
import dungeon.gui.GameCellView;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.event.ActionEvent;

/**
 * Controller class for the MiniDungeon GUI.
 * Connects the back-end game engine to the JavaFX front-end.
 */
public class Controller {

    // GridPane that displays the 10x10 dungeon map
    @FXML private GridPane gridPane;

    // Movement buttons linked via FXML
    @FXML private Button btnUp;
    @FXML private Button btnDown;
    @FXML private Button btnLeft;
    @FXML private Button btnRight;
    @FXML private Label lblHP;
    @FXML private Label lblScore;
    @FXML private Label lblSteps;

    // Game engine instance handling logic
    GameEngine engine;

    /**
     * Called automatically when the FXML is loaded.
     * Initializes the game engine and fills the GUI grid.
     */
    @FXML
    public void initialize() {
        engine = new GameEngine(10); // Create a 10x10 dungeon
        updateGui();                 // Draw the initial grid
        lblHP.setText("HP: " + engine.getPlayer().getHealth());
        lblScore.setText("Score: " + engine.getPlayer().getScore());
        lblSteps.setText("Steps Left: " + engine.getPlayer().getStepsLeft());
    }

    // Handle UP button click
    @FXML
    private void handleUp(ActionEvent event) {
        processMove("up");
    }

    // Handle DOWN button click
    @FXML
    private void handleDown(ActionEvent event) {
        processMove("down");
    }

    // Handle LEFT button click
    @FXML
    private void handleLeft(ActionEvent event) {
        processMove("left");
    }

    // Handle RIGHT button click
    @FXML
    private void handleRight(ActionEvent event) {
        processMove("right");
    }

    /**
     * Moves the player in the given direction,
     * applies cell interactions, and updates the display.
     */
    private void processMove(String direction) {
        boolean moved = engine.movePlayer(direction);

        if (moved) {
            GameCell current = engine.getMap()[engine.getPlayer().getRow()][engine.getPlayer().getCol()];
            String message = current.onEnter(engine.getPlayer());
            System.out.println(message); // Display message in console (can be shown in GUI later)
        }

        updateGui(); // Redraw the map after each move
    }

    /**
     * Loops through the game map and renders each GameCell as a GameCellView in the GUI.
     */
    private void updateGui() {
        gridPane.getChildren().clear(); // Remove old cells

        for (int i = 0; i < engine.getSize(); i++) {
            for (int j = 0; j < engine.getSize(); j++) {
                GameCell cell = engine.getMap()[i][j];
                GameCellView cellView = new GameCellView(cell);
                gridPane.add(cellView, j, i); // (column, row)
            }
        }

        gridPane.setGridLinesVisible(true); // Show grid lines

        // ✅ Update HUD labels
        lblHP.setText("HP: " + engine.getPlayer().getHealth());
        lblScore.setText("Score: " + engine.getPlayer().getScore());
        lblSteps.setText("Steps Left: " + engine.getPlayer().getStepsLeft());
    }
}