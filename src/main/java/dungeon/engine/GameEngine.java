package dungeon.engine;

import javafx.scene.text.Text;

/**
 * Represents the core logic for the MiniDungeon game.
 */
public class GameEngine {

    private GameCell[][] map;
    private int size;
    private Player player;

    /**
     * Creates a square game board and initializes the player.
     *
     * @param size the width and height of the square map
     */
    public GameEngine(int size) {
        this.size = size;
        map = new GameCell[size][size];
        this.player = new Player(0, 0); // Starting in top-left corner

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                GameCell cell = new EmptyCell(i, j); // no visual Text needed
                map[i][j] = cell;
            }
        }

        // Removed JavaFX styling lines — not used in logic-only engine
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
    public GameCell[][] getMap() {
        return map;
    }

    public Player getPlayer() {
        return player;
    }


    /**
     * Moves the player if possible in the specified direction.
     *
     * @param direction up, down, left, or right
     * @return true if the move was valid and completed
     */
    public boolean movePlayer(String direction) {
        int x = player.getRow();
        int y = player.getCol();

        int newX = x;
        int newY = y;

        switch (direction.toLowerCase()) {
            case "up": newX--; break;
            case "down": newX++; break;
            case "left": newY--; break;
            case "right": newY++; break;
            default:
                System.out.println("Invalid direction: " + direction);
                return false;
        }

        // Bounds check
        if (newX < 0 || newX >= size || newY < 0 || newY >= size) {
            System.out.println("You can't move outside the map.");
            return false;
        }

        // Move the player
        player.moveTo(newX, newY);
        System.out.println("Moved " + direction + " to (" + newX + "," + newY + ")");

        // After moving: check for ranged mutant attacks
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                GameCell cell = map[i][j];
                if (cell instanceof RangedMutantCell) {
                    int dist = Math.abs(i - newX) + Math.abs(j - newY);
                    if ((i == newX || j == newY) && dist <= 2 && dist > 0) {
                        String result = ((RangedMutantCell) cell).tryAttack(player);
                        if (result != null) {
                            System.out.println(result);
                        }
                    }
                }
            }
        }

        return true;
    }

    /**
     * Runs a simple test of movement in text mode.
     */
    public static void main(String[] args) {
        GameEngine engine = new GameEngine(10);
        System.out.printf("MiniDungeon Engine v1.2 — Map size is %d × %d\n", engine.getSize(), engine.getSize());

        // Place a GoldCell at (0,1)
        engine.getMap()[0][1] = new GoldCell(0, 1);

        // Place a TrapCell at (0,2)
        engine.getMap()[0][2] = new TrapCell(0, 2);

        // Move to GoldCell
        boolean moved1 = engine.movePlayer("right");
        if (moved1) {
            GameCell cell = engine.getMap()[0][1];
            System.out.println(cell.onEnter(engine.getPlayer()));
            System.out.println("Score after gold: " + engine.getPlayer().getScore());
        }

        // Move to TrapCell
        boolean moved2 = engine.movePlayer("right");
        if (moved2) {
            GameCell cell = engine.getMap()[0][2];
            System.out.println(cell.onEnter(engine.getPlayer()));
            System.out.println("HP after trap: " + engine.getPlayer().getHealth());
        }
        // Set HP to 6 manually to test healing
        engine.getPlayer().loseHP(2); // drops HP to 6

        // Place PotionCell at (0,3)
        engine.getMap()[0][3] = new PotionCell(0, 3);

        // Move to PotionCell
        boolean moved3 = engine.movePlayer("right");
        if (moved3) {
            GameCell cell = engine.getMap()[0][3];
            System.out.println(cell.onEnter(engine.getPlayer()));
            System.out.println("HP after potion: " + engine.getPlayer().getHealth());
        }
        // Place MeleeMutantCell at (0,4)
        engine.getMap()[0][4] = new MeleeMutantCell(0, 4);

        // Move to MeleeMutantCell
        boolean moved4 = engine.movePlayer("right");
        if (moved4) {
            GameCell cell = engine.getMap()[0][4];
            System.out.println(cell.onEnter(engine.getPlayer()));
            System.out.println("HP after mutant: " + engine.getPlayer().getHealth());
            System.out.println("Score after mutant: " + engine.getPlayer().getScore());
        }
        // Place a RangedMutantCell at (2,4) – 2 rows below the player
        engine.getMap()[2][4] = new RangedMutantCell(2, 4);

        // Move down to (1,4) – triggers proximity (still within 2 vertical tiles)
        boolean moved5 = engine.movePlayer("down");
        if (moved5) {
            GameCell cell = engine.getMap()[1][4];
            System.out.println("Standing at: " + engine.getPlayer().getRow() + "," + engine.getPlayer().getCol());
        }

    }



}