package dungeon.engine;

/**
 * Represents the player in the MiniDungeon game.
 * Tracks position, health, score, and steps remaining.
 */
public class Player {
    private int row;
    private int col;
    private int health;
    private int score;
    private int stepsLeft;

    /**
     * Creates a player starting at a given position.
     *
     * @param startRow initial row position
     * @param startCol initial column position
     */
    public Player(int startRow, int startCol) {
        this.row = startRow;
        this.col = startCol;
        this.health = 10;
        this.score = 0;
        this.stepsLeft = 100;
    }

    // Position
    public int getRow() { return row; }
    public int getCol() { return col; }

    public void moveTo(int newRow, int newCol) {
        this.row = newRow;
        this.col = newCol;
        this.stepsLeft--;
    }

    // Health
    public int getHealth() { return health; }
    public void loseHP(int amount) { health -= amount; }
    public void gainHP(int amount) {
        health += amount;
        if (health > 10) health = 10;
    }

    // Score
    public int getScore() { return score; }
    public void addScore(int points) { score += points; }

    // Steps
    public int getStepsLeft() { return stepsLeft; }

    // Game status checks
    public boolean isAlive() { return health > 0; }
    public boolean hasSteps() { return stepsLeft > 0; }
}