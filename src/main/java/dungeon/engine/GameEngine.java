package dungeon.engine;

import java.io.Serializable;

/**
 * Represents the core logic for the MiniDungeon game.
 */
public class GameEngine implements Serializable {
    private static final long serialVersionUID = 1L;

    private GameCell[][] map;
    private int size;
    private Player player;

    public GameEngine(int size) {
        this.size = size;
        this.player = new Player(0, 0);
        map = new GameCell[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                map[i][j] = new EmptyCell(i, j);
            }
        }

        map[0][1] = new GoldCell(0, 1);
        map[0][2] = new TrapCell(0, 2);
        map[0][3] = new PotionCell(0, 3);
        map[0][4] = new MeleeMutantCell(0, 4);
        map[2][4] = new RangedMutantCell(2, 4);
    }

    public int getSize() {
        return size;
    }

    public GameCell[][] getMap() {
        return map;
    }

    public Player getPlayer() {
        return player;
    }

    public boolean movePlayer(String direction) {
        int x = player.getRow();
        int y = player.getCol();
        int newX = x, newY = y;

        switch (direction.toLowerCase()) {
            case "up" -> newX--;
            case "down" -> newX++;
            case "left" -> newY--;
            case "right" -> newY++;
            default -> {
                System.out.println("Invalid direction: " + direction);
                return false;
            }
        }

        if (newX < 0 || newX >= size || newY < 0 || newY >= size) {
            System.out.println("You can't move outside the map.");
            return false;
        }

        player.moveTo(newX, newY);
        System.out.println("Moved " + direction + " to (" + newX + "," + newY + ")");

        // Ranged attacks
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                GameCell cell = map[i][j];
                if (cell instanceof RangedMutantCell ranged) {
                    int dist = Math.abs(i - newX) + Math.abs(j - newY);
                    if ((i == newX || j == newY) && dist <= 2 && dist > 0) {
                        String result = ranged.tryAttack(player);
                        if (result != null) {
                            System.out.println(result);
                        }
                    }
                }
            }
        }

        return true;
    }
}