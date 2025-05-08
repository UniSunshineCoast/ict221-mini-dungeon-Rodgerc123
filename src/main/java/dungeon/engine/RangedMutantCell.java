package dungeon.engine;

import java.util.Random;

/**
 * A ranged mutant that can attack from a distance.
 */
public class RangedMutantCell extends GameCell {
    private boolean defeated = false;
    private static final Random random = new Random();

    public RangedMutantCell(int row, int col) {
        super(row, col);
    }

    @Override
    public char getSymbol() {
        return defeated ? '.' : 'R';
    }

    @Override
    public String onEnter(Player player) {
        if (!defeated) {
            player.addScore(2);
            defeated = true;
            return "You attacked a ranged mutant and won! You gained 2 score.";
        }
        return "You see the remains of a defeated ranged mutant.";
    }

    /**
     * Called each time the player moves. 50% chance to hit.
     */
    public String tryAttack(Player player) {
        if (!defeated && random.nextBoolean()) {
            player.loseHP(2);
            return "A ranged mutant attacked and you lost 2 HP!";
        } else if (!defeated) {
            return "A ranged mutant attacked, but missed.";
        }
        return null;
    }
}