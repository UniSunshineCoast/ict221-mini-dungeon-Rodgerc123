package dungeon.gui;

import dungeon.engine.GameCell;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class GameCellView extends StackPane {

    private GameCell cell;

    public GameCellView(GameCell cell) {
        this.cell = cell;

        // Display the symbol (we can improve this later)
        Label label = new Label(String.valueOf(cell.getSymbol()));
        getChildren().add(label);

        // Optional: set size or style
        setPrefSize(40, 40);
        setStyle("-fx-border-color: black; -fx-alignment: center;");
    }

    public GameCell getCell() {
        return cell;
    }
}