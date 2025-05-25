package dungeon.engine;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for RangedMutantCell behavior.
 */
public class RangedMutantCellTest {

    @Test
    public void testSteppingOnRangedMutant() {
        Player player = new Player(0, 0);
        RangedMutantCell mutant = new RangedMutantCell(0, 1);

        assertEquals(0, player.getScore());
        assertEquals(10, player.getHealth());
        assertEquals('R', mutant.getSymbol());

        // Step on it
        String result1 = mutant.onEnter(player);
        assertEquals("You attacked a ranged mutant and won! You gained 2 score.", result1);
        assertEquals(2, player.getScore());
        assertEquals(10, player.getHealth()); // no damage
        assertEquals('.', mutant.getSymbol());

        // Second entry
        String result2 = mutant.onEnter(player);
        assertEquals("You see the remains of a defeated ranged mutant.", result2);
        assertEquals(2, player.getScore());
    }

    @Test
    public void testRangedAttackRandomness() {
        Player player = new Player(0, 0);
        RangedMutantCell mutant = new RangedMutantCell(1, 0);

        int originalHP = player.getHealth();
        int hits = 0;
        int misses = 0;

        // Simulate 10 attacks
        for (int i = 0; i < 10; i++) {
            int beforeHP = player.getHealth();
            String outcome = mutant.tryAttack(player);

            if (outcome != null) {
                if (outcome.contains("lost")) {
                    hits++;
                } else if (outcome.contains("missed")) {
                    misses++;
                }
            }
        }

        // Just confirm it actually fired at least once
        assertTrue(hits + misses > 0);
        assertTrue(hits >= 0 && misses >= 0);
        assertTrue(player.getHealth() <= originalHP);
    }
}