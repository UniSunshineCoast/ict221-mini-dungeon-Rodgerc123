package dungeon.engine;


/**
 * A cell that restores the player's health when entered.
 * Disappears after being used.
 */
public class PotionCell extends GameCell {
    private static final long serialVersionUID = 1L;

    private boolean used = false;

    public PotionCell(int row, int col) {
        super(row, col);
    }

    @Override
    public char getSymbol() {
        return used ? '.' : 'H';
    }

    @Override
    public String onEnter(Player player) {
        if (!used) {
            player.gainHP(4);
            used = true;
            return "You drank a health potion and gained 4 HP.";
        }
        return "An empty potion bottle lies here.";
    }
}