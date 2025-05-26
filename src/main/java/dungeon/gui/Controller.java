package dungeon.gui;

import dungeon.engine.GameCell;
import dungeon.engine.GameEngine;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
    @FXML private Label lblHP;
    @FXML private Label lblScore;
    @FXML private Label lblSteps;

    private GameEngine engine;

    @FXML
    public void initialize() {
        engine = new GameEngine(10);
        gridPane.setMinSize(640, 640);
        gridPane.setMaxSize(640, 640);
        gridPane.setPrefSize(640, 640);
        gridPane.setHgap(0);
        gridPane.setVgap(0);
        gridPane.setPadding(Insets.EMPTY);
        setGridBackground();
        updateGui();
        updateStats();
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
            System.err.println("❌ Save failed: " + e.getMessage());
        }
    }

    @FXML
    private void handleLoad(ActionEvent event) {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("savegame.dat"))) {
            engine = (GameEngine) in.readObject();
            updateGui();
            updateStats();
            System.out.println("✅ Game loaded.");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("❌ Load failed: " + e.getMessage());
        }
    }

    private void processMove(String direction) {
        boolean moved = engine.movePlayer(direction);
        if (moved) {
            GameCell cell = engine.getMap()[engine.getPlayer().getRow()][engine.getPlayer().getCol()];
            System.out.println(cell.onEnter(engine.getPlayer()));
        }
        updateGui();
        updateStats();
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