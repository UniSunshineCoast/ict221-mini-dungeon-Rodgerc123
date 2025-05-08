package dungeon.engine;

/**
 * A trap cell that harms the player when entered.
 * The trap remains active after triggering.
 */
public class TrapCell extends GameCell {

    public TrapCell(int row, int col) {
        super(row, col);
    }

    @Override
    public char getSymbol() {
        return 'T';
    }

    @Override
    public String onEnter(Player player) {
        player.loseHP(2);
        return "You fell into a trap and lost 2 HP!";
    }
}
