package dungeon.engine;

import javafx.scene.text.Text;

/**
 * Represents the core logic for the MiniDungeon game.
 */
public class GameEngine {

    private Cell[][] map;
    private int size;
    private Player player;

    /**
     * Creates a square game board and initializes the player.
     *
     * @param size the width and height of the square map
     */
    public GameEngine(int size) {
        this.size = size;
        map = new Cell[size][size];
        this.player = new Player(0, 0); // Starting in top-left corner

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Cell cell = new Cell();
                Text text = new Text(i + "," + j);
                cell.getChildren().add(text);
                map[i][j] = cell;
            }
        }

        map[0][0].setStyle("-fx-background-color: #7baaa4");
        map[size - 1][size - 1].setStyle("-fx-background-color: #7baaa4");
    }

    /**
     * Returns the map size (width and height).
     */
    public int getSize() {
        return map.length;
    }

    /**
     * Returns the map array.
     */
    public Cell[][] getMap() {
        return map;
    }

    /**
     * Moves the player if possible in the specified direction.
     *
     * @param direction up, down, left, or right
     * @return true if the move was valid and completed
     */
    public boolean movePlayer(String direction) {
        int row = player.getRow();
        int col = player.getCol();
        int newRow = row;
        int newCol = col;

        switch (direction.toLowerCase()) {
            case "up": newRow--; break;
            case "down": newRow++; break;
            case "left": newCol--; break;
            case "right": newCol++; break;
            default:
                System.out.println("Invalid direction: " + direction);
                return false;
        }

        if (newRow < 0 || newCol < 0 || newRow >= size || newCol >= size) {
            System.out.println("You can't move outside the map!");
            return false;
        }

        player.moveTo(newRow, newCol);
        System.out.println("Moved " + direction + " to (" + newRow + "," + newCol + ")");
        return true;
    }

    /**
     * Runs a simple test of movement in text mode.
     */
    public static void main(String[] args) {
        GameEngine engine = new GameEngine(10);
        System.out.printf("The size of map is %d * %d\n", engine.getSize(), engine.getSize());

        engine.movePlayer("right");
        engine.movePlayer("down");
        engine.movePlayer("up");
        engine.movePlayer("left");
        engine.movePlayer("invalid"); // invalid direction test
    }
}