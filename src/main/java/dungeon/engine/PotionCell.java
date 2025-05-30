package dungeon.engine;


/**
 * A cell that restores the player's health when entered.
 * Disappears after being used.
 */

public class PotionCell extends GameCell {
    private static final long serialVersionUID = 1L;
    private boolean consumed = false;

    public PotionCell(int row, int col) {
        super(row, col);
    }

    @Override
    public char getSymbol() {
        return consumed ? '.' : 'H';
    }

    @Override
    public String onEnter(Player player) {
        if (!consumed) {
            player.gainHP(3);  // Regains up to 3 HP
            consumed = true;
            return "You found a potion and gained 3 HP!";
        }
        return "You see the remains of a used potion.";
    }
}