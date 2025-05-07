package dungeon.engine;

import javafx.scene.text.Text;

public class GameEngine {

    /**
     * An example board to store the current game state.
     */
    private Cell[][] map;
    private int size;
    private Player player;


    /**
     * Creates a square game board.
     *
     * @param size the width and height.
     */

    public GameEngine(int size) {
        this.size = size; // Store the size
        map = new Cell[size][size];
        this.player = new Player(0, 0); // Initialize the player

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Cell cell = new Cell();
                Text text = new Text(i + "," + j);
                cell.getChildren().add(text);
                map[i][j] = cell;
            }
        }

        map[0][0].setStyle("-fx-background-color: #7baaa4");
        map[size-1][size-1].setStyle("-fx-background-color: #7baaa4");
    }


    /**
     * The size of the current game.
     *
     * @return this is both the width and the height.
     */
    public int getSize() {
        return map.length;
    }

    /**
     * The map of the current game.
     *
     * @return the map, which is a 2d array.
     */
    public Cell[][] getMap() {
        return map;
    }

    /**
     * Plays a text-based game
     */
    public static void main(String[] args) {
        GameEngine engine = new GameEngine(10);
        System.out.printf("The size of map is %d * %d\n", engine.getSize(), engine.getSize());
    }
}
