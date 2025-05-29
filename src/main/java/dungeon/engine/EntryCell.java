package dungeon.engine;

/**
 * EntryCell represents the starting door or entry point into the dungeon.
 * It does nothing onEnter but serves as a visual or symbolic entrance.
 */
public class EntryCell extends GameCell {
    public EntryCell(int row, int col) {
        super(row, col);
    }

    @Override
    public char getSymbol() {
        return 'E';  // Entry icon (used for visuals)
    }

    @Override
    public String onEnter(Player player) {
        return "You are at the dungeon entrance.";
    }
}