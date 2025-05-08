package dungeon.engine;

/**
 * A stationary melee mutant.
 * Stepping on it costs 2 HP but gives 2 score.
 * The mutant is defeated and removed after encounter.
 */
public class MeleeMutantCell extends GameCell {
    private boolean defeated = false;

    public MeleeMutantCell(int row, int col) {
        super(row, col);
    }

    @Override
    public char getSymbol() {
        return defeated ? '.' : 'M';
    }

    @Override
    public String onEnter(Player player) {
        if (!defeated) {
            player.loseHP(2);
            player.addScore(2);
            defeated = true;
            return "You attacked a melee mutant and won! You gained 2 score but lost 2 HP.";
        }
        return "The remains of a defeated mutant lie here.";
    }
}