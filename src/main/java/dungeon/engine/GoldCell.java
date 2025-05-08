package dungeon.engine;

/**
 * A cell that gives the player gold (score points) and then disappears.
 */
public class GoldCell extends GameCell {
    private boolean collected = false;

    public GoldCell(int row, int col) {
        super(row, col);
    }

    @Override
    public char getSymbol() {
        return collected ? '.' : 'G';
    }

    @Override
    public String onEnter(Player player) {
        if (!collected) {
            player.addScore(2);
            collected = true;
            return "You picked up a gold.";
        }
        return "Nothing here.";
    }
}