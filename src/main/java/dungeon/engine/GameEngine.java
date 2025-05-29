package dungeon.engine;

import java.io.Serializable;
import java.util.Random;

/**
 * The core engine for the MiniDungeon game.
 * Handles map generation, player movement, game progression, and state.
 */
public class GameEngine implements Serializable {
    private static final long serialVersionUID = 1L;

    private GameCell[][] map;
    private Player player;
    private int size;
    private int difficulty;
    private int currentLevel;
    private String playerName;

    private boolean gameWon = false;
    private boolean gameOver = false;

    public GameEngine(int size, int difficulty, String playerName) {
        this.size = size;
        this.difficulty = difficulty;
        this.playerName = playerName;
        this.currentLevel = 1;
        generateLevel();
    }

    /**
     * Generates the dungeon map for the current level based on difficulty.
     */
    public void generateLevel() {
        map = new GameCell[size][size];
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                map[row][col] = new EmptyCell(row, col); // Ensures no null cells
            }
        }

        Random rand = new Random();

        // Place entry door at bottom-left
        int entryRow = size - 1;
        int entryCol = 0;
        map[entryRow][entryCol] = new EntryCell(entryRow, entryCol);

        // Place player just to the right of the door
        int playerRow = entryRow;
        int playerCol = entryCol + 1;
        player = new Player(playerRow, playerCol);

        int itemCount = difficulty + 2;

        // Add gold
        for (int i = 0; i < itemCount; i++) {
            placeRandomCell(new GoldCell(0, 0));
        }

        // Add trap
        for (int i = 0; i < itemCount; i++) {
            placeRandomCell(new TrapCell(0, 0));
        }

        // Add melee mutant
        for (int i = 0; i < itemCount / 2; i++) {
            placeRandomCell(new MeleeMutantCell(0, 0));
        }

        // Add ranged mutant
        for (int i = 0; i < itemCount / 2; i++) {
            placeRandomCell(new RangedMutantCell(0, 0));
        }

        // Add potions
        for (int i = 0; i < 2; i++) {
            placeRandomCell(new PotionCell(0, 0));
        }

        // Place ladder
        placeRandomCell(new LadderCell(0, 0));
    }

    /**
     * Helper to place a GameCell on an empty cell only.
     */
    private void placeRandomCell(GameCell cell) {
        Random rand = new Random();
        int row, col;
        do {
            row = rand.nextInt(size);
            col = rand.nextInt(size);
        } while (
                (row == player.getRow() && col == player.getCol()) ||  // avoid player
                        (map[row][col] != null && !(map[row][col] instanceof EmptyCell)) // avoid non-empty
        );
        cell.setRow(row);
        cell.setCol(col);
        map[row][col] = cell;
    }

    /**
     * Attempts to move the player in a direction and handles map interactions.
     */
    public boolean movePlayer(String direction) {
        if (gameWon || gameOver) return false;

        int newRow = player.getRow();
        int newCol = player.getCol();

        switch (direction) {
            case "up" -> newRow--;
            case "down" -> newRow++;
            case "left" -> newCol--;
            case "right" -> newCol++;
        }

        if (newRow < 0 || newRow >= size || newCol < 0 || newCol >= size) return false;

        // Move and interact
        player.moveTo(newRow, newCol);
        GameCell cell = map[newRow][newCol];

        if (cell != null) {
            String message = cell.onEnter(player);
            System.out.println(message);

            if (cell instanceof LadderCell) {
                if (currentLevel == 1) {
                    // Go to next level
                    currentLevel = 2;
                    difficulty += 2;
                    generateLevel();
                    System.out.println("You've reached Level 2! Difficulty increased.");
                } else {
                    gameWon = true;
                    System.out.println("Congratulations! You completed the dungeon!");
                }
            }
        }

        if (!player.isAlive() || !player.hasSteps()) {
            gameOver = true;
            player.setScore(-1);
            System.out.println("Game Over. You lost.");
        }

        return true;
    }

    // === Getters ===
    public GameCell[][] getMap() { return map; }
    public Player getPlayer() { return player; }
    public int getSize() { return size; }
    public int getCurrentLevel() { return currentLevel; }
    public boolean isGameWon() { return gameWon; }
    public boolean isGameOver() { return gameOver; }
    public String getPlayerName() { return playerName; }
}