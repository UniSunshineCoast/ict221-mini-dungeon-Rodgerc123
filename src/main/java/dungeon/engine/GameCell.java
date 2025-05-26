package dungeon.engine;

import java.io.Serializable;

/**
 * Abstract class representing a special cell in the dungeon.
 * Subclasses define specific interactions (e.g., gold, trap, mutant).
 */
public abstract class GameCell implements Serializable {
    private static final long serialVersionUID = 1L;

    protected int row;
    protected int col;

    public GameCell(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() { return row; }
    public int getCol() { return col; }

    /**
     * Returns the single-character symbol for this cell on the map.
     */
    public abstract char getSymbol();

    /**
     * Called when the player enters this cell.
     * Each cell type implements its own interaction.
     */
    public abstract String onEnter(Player player);
}