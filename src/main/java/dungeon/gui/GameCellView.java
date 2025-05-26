package dungeon.gui;

import dungeon.engine.GameCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

/**
 * Represents a single visual cell in the GUI dungeon grid.
 * Loads the appropriate image based on cell type,
 * and overlays the player icon if the player is in this cell.
 */
public class GameCellView extends StackPane {

    /**
     * Constructs a GUI view for a dungeon cell, with an optional player overlay.
     */
    public GameCellView(GameCell cell, boolean hasPlayer) {
        setPrefSize(64, 64); // Match icon size

        // Base icon for the cell (e.g., gold, trap, wall)
        String iconFile = getIconForCell(cell);
        if (iconFile != null) {
            ImageView cellIcon = new ImageView(loadImage(iconFile));
            cellIcon.setFitWidth(48);
            cellIcon.setFitHeight(48);
            getChildren().add(cellIcon);
        }

        // If the player is on this cell, overlay the player icon
        if (hasPlayer) {
            ImageView playerIcon = new ImageView(loadImage("player.png"));
            playerIcon.setFitWidth(48);
            playerIcon.setFitHeight(48);
            getChildren().add(playerIcon);
        }
    }

    /**
     * Maps a GameCell symbol to its corresponding icon filename.
     * Empty or unknown cells return null (render no icon).
     */
    private String getIconForCell(GameCell cell) {
        char symbol = cell.getSymbol();
        return switch (symbol) {
            case 'G' -> "gold.png";
            case 'T' -> "trap.png";
            case 'M' -> "melee_mutant.png";
            case 'R' -> "ranged_mutant.png";
            case 'H' -> "potion.png";
            case 'L' -> "ladder.png";
            case 'E' -> "entry.png";
            case '#' -> "wall.png";
            default -> null;  // ✅ No icon for EmptyCell
        };
    }

    /**
     * Loads an image from the /images/ folder in resources.
     */
    private Image loadImage(String filename) {
        return new Image(getClass().getResource("/images/" + filename).toExternalForm());
    }
}
