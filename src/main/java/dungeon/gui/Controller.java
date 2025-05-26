package dungeon.gui;

import dungeon.engine.GameCell;
import dungeon.engine.GameEngine;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

public class Controller {

    // GUI controls linked from FXML
    @FXML private GridPane gridPane;
    @FXML private Button btnUp;
    @FXML private Button btnDown;
    @FXML private Button btnLeft;
    @FXML private Button btnRight;
    @FXML private Label lblHP;
    @FXML private Label lblScore;
    @FXML private Label lblSteps;

    // Game logic handler
    private GameEngine engine;

    /**
     * Called when the FXML layout is loaded.
     */
    @FXML
    public void initialize() {
        engine = new GameEngine(10);  // Create a 10x10 map

        // Fix layout sizing and spacing
        gridPane.setMinSize(640, 640);
        gridPane.setPrefSize(640, 640);
        gridPane.setMaxSize(640, 640);
        gridPane.setHgap(0);
        gridPane.setVgap(0);
        gridPane.setPadding(Insets.EMPTY);

        setGridBackground();   // Apply background texture
        updateGui();           // Populate grid cells
        updateStats();         // Initialize status labels
    }

    @FXML private void handleUp(ActionEvent event)    { processMove("up"); }
    @FXML private void handleDown(ActionEvent event)  { processMove("down"); }
    @FXML private void handleLeft(ActionEvent event)  { processMove("left"); }
    @FXML private void handleRight(ActionEvent event) { processMove("right"); }

    /**
     * Processes a move and triggers GUI updates.
     */
    private void processMove(String direction) {
        boolean moved = engine.movePlayer(direction);

        if (moved) {
            GameCell current = engine.getMap()[engine.getPlayer().getRow()][engine.getPlayer().getCol()];
            String message = current.onEnter(engine.getPlayer());
            System.out.println(message);  // Optional: show in console or future GUI label
        }

        updateGui();
        updateStats();
    }

    /**
     * Updates the gridPane with current map and player view.
     */
    private void updateGui() {
        gridPane.getChildren().clear();

        for (int i = 0; i < engine.getSize(); i++) {
            for (int j = 0; j < engine.getSize(); j++) {
                GameCell cell = engine.getMap()[i][j];
                boolean hasPlayer = (engine.getPlayer().getRow() == i && engine.getPlayer().getCol() == j);
                GameCellView cellView = new GameCellView(cell, hasPlayer);
                gridPane.add(cellView, j, i); // (column, row)
            }
        }

        gridPane.setGridLinesVisible(true);
    }

    /**
     * Sets a fixed background image aligned with the 10x10 grid.
     */
    private void setGridBackground() {
        Image bgImage = new Image(getClass().getResource("/images/background.png").toExternalForm());

        BackgroundImage backgroundImage = new BackgroundImage(
                bgImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                new BackgroundPosition(Side.LEFT, 0, false, Side.TOP, 0, false),
                new BackgroundSize(640, 640, false, false, false, false)
        );

        gridPane.setBackground(new Background(backgroundImage));
    }

    /**
     * Updates the labels for health, score, and remaining steps.
     */
    private void updateStats() {
        lblHP.setText("HP: " + engine.getPlayer().getHealth());
        lblScore.setText("Score: " + engine.getPlayer().getScore());
        lblSteps.setText("Steps Left: " + engine.getPlayer().getStepsLeft());
    }
}