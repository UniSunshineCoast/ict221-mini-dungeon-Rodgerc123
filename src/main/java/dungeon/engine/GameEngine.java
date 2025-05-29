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
    // private boolean topScoreAchieved = false;

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
                map[row][col] = new EmptyCell(row, col);
            }
        }

        Random rand = new Random();

        // Place entry
        int entryRow = size - 1;
        int entryCol = 0;
        map[entryRow][entryCol] = new EntryCell(entryRow, entryCol); // <-- Might crash here

        // Place player to the right
        player = new Player(entryRow, entryCol, playerName);

        int itemCount = difficulty + 2;

        // Add gold
        for (int i = 0; i < itemCount; i++) placeRandomCell(new GoldCell(rand.nextInt(size), rand.nextInt(size)));

        // Add trap
        for (int i = 0; i < itemCount; i++) placeRandomCell(new TrapCell(rand.nextInt(size), rand.nextInt(size)));

        // Add melee mutant
        for (int i = 0; i < itemCount / 2; i++) placeRandomCell(new MeleeMutantCell(rand.nextInt(size), rand.nextInt(size)));

        // Add ranged mutant
        for (int i = 0; i < itemCount / 2; i++) placeRandomCell(new RangedMutantCell(rand.nextInt(size), rand.nextInt(size)));

        // Add potion
        for (int i = 0; i < 2; i++) placeRandomCell(new PotionCell(rand.nextInt(size), rand.nextInt(size)));

        // Add ladder
        placeRandomCell(new LadderCell(rand.nextInt(size), rand.nextInt(size)));
    }

    /**
     * Helper to place a GameCell on an empty space only.
     */
    private void placeRandomCell(GameCell cell) {
        Random rand = new Random();
        int row, col;
        int attempts = 0;
        final int MAX_ATTEMPTS = 100;

        do {
            row = rand.nextInt(size);
            col = rand.nextInt(size);
            attempts++;
            if (attempts > MAX_ATTEMPTS) {
                System.err.println("⚠ Failed to place " + cell.getClass().getSimpleName() + " after " + MAX_ATTEMPTS + " attempts.");
                return;
            }
        } while ((row == player.getRow() && col == player.getCol()) || !(map[row][col] instanceof EmptyCell));

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

        player.moveTo(newRow, newCol);

        GameCell cell = map[newRow][newCol];
        if (cell != null) {
            String message = cell.onEnter(player);
            System.out.println(message);

            if (cell instanceof LadderCell) {
                if (currentLevel == 1) {
                    currentLevel = 2;
                    difficulty += 2;
                    generateLevel();
                    System.out.println("You've reached Level 2! Difficulty increased.");
                } else {
                    gameWon = true;
                    System.out.println("🎉 Congratulations! You completed the dungeon!");

                    // Save score
                    //player.setScore(player.getScore()); // Final score set already via cell interactions
                    //topScoreAchieved = ScoreManager.addScore(playerName, player.getScore());
                    //System.out.println(topScoreAchieved ? "🏆 New Top Score!" : "Thanks for playing!");

                }
            }
        }

        if (!player.isAlive() || !player.hasSteps()) {
            gameOver = true;
            player.setScore(-1); // fail state
            System.out.println("💀 Game Over. You lost.");

            // Save fail score too (not top likely)
            // topScoreAchieved = ScoreManager.addScore(playerName, player.getScore());
        }

        return true;
    }

    // === Getters ===
    public GameCell[][] getMap() { return map; }
    public Player getPlayer() { return player; }
    public int getSize() { return size; }
    public int getCurrentLevel() { return currentLevel; }
    public boolean isGameWon() { return gameWon; }
    public String getPlayerName() { return playerName; }
    public int getDifficulty() { return difficulty; }
    /**
     * Returns true if the game has ended — player has no health or steps, or reached the final level.
     */
    public boolean isGameOver() {
        return player.getHealth() <= 0 || player.getStepsLeft() <= 0 || gameWon;
    }
    // public boolean isTopScoreAchieved() { return topScoreAchieved; }
}