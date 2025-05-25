package dungeon.engine;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for MeleeMutantCell interaction.
 */
public class MeleeMutantCellTest {

    @Test
    public void testMeleeMutantInteraction() {
        Player player = new Player(0, 0);
        MeleeMutantCell mutant = new MeleeMutantCell(0, 1);

        assertEquals(10, player.getHealth());
        assertEquals(0, player.getScore());
        assertEquals('M', mutant.getSymbol());

        // First encounter
        String result1 = mutant.onEnter(player);
        assertEquals("You attacked a melee mutant and won! You gained 2 score but lost 2 HP.", result1);
        assertEquals(8, player.getHealth());
        assertEquals(2, player.getScore());
        assertEquals('.', mutant.getSymbol());

        // Second encounter (mutant already defeated)
        String result2 = mutant.onEnter(player);
        assertEquals("The remains of a defeated mutant lie here.", result2);
        assertEquals(8, player.getHealth()); // no further HP loss
        assertEquals(2, player.getScore());  // no additional score
    }
}