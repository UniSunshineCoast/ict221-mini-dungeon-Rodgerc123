package dungeon.engine;

import java.io.Serializable;

/**
 * A stationary melee mutant.
 * Stepping on it costs 2 HP but gives 2 score.
 * The mutant is defeated and removed after encounter.
 */
public class MeleeMutantCell extends GameCell implements Serializable {
    private static final long serialVersionUID = 1L;

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
        return "You see the remains of a melee mutant.";
    }
}