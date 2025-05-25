package dungeon.gui;

import dungeon.engine.GameCell;
import dungeon.engine.GameEngine;
import dungeon.gui.GameCellView;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;

public class Controller {
    @FXML
    private GridPane gridPane;

    GameEngine engine;

    @FXML
    public void initialize() {
        engine = new GameEngine(10);
        updateGui();
    }

    private void updateGui() {
        //Clear old GUI grid pane
        gridPane.getChildren().clear();

        //Loop through map board and add each cell into grid pane
        for (int i = 0; i < engine.getSize(); i++) {
            for (int j = 0; j < engine.getSize(); j++) {
                GameCell cell = engine.getMap()[i][j];
                GameCellView cellView = new GameCellView(cell);
                gridPane.add(cellView, j, i);
            }
        }

        gridPane.setGridLinesVisible(true);
    }
}
