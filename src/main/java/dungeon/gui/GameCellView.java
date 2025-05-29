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
     * @param cell the GameCell object representing this cell (can be null)
     * @param hasPlayer true if the player is currently on this cell
     */
    public GameCellView(GameCell cell, boolean hasPlayer) {
        setPrefSize(64, 64); // Match icon size to grid

        // Handle null cells gracefully — shows no background if cell is null
        if (cell != null) {
            String iconFile = getIconForCell(cell);
            if (iconFile != null) {
                ImageView cellIcon = new ImageView(loadImage(iconFile));
                cellIcon.setFitWidth(48);
                cellIcon.setFitHeight(48);
                getChildren().add(cellIcon);
            }
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
     * Returns null if the cell is null or unknown (no image shown).
     */
    private String getIconForCell(GameCell cell) {
        if (cell == null) return null; // 🔒 Prevents crash
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
            default -> null; // No icon for unknown or empty cells
        };
    }

    /**
     * Loads an image from the /images/ folder in resources.
     * @param filename the image file name (e.g., "trap.png")
     * @return JavaFX Image object
     */
    private Image loadImage(String filename) {
        return new Image(getClass().getResource("/images/" + filename).toExternalForm());
    }
}