package dungeon.engine;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for TrapCell interaction.
 */
public class TrapCellTest {

    @Test
    public void testTrapCellReducesHPAndStaysActive() {
        Player player = new Player(0, 0);
        TrapCell trap = new TrapCell(1, 1);

        assertEquals(10, player.getHealth());
        assertEquals('T', trap.getSymbol());

        // First trap encounter
        String result1 = trap.onEnter(player);
        assertEquals("You fell into a trap and lost 2 HP!", result1);
        assertEquals(8, player.getHealth());
        assertEquals('T', trap.getSymbol());

        // Second trap encounter (still active)
        String result2 = trap.onEnter(player);
        assertEquals("You fell into a trap and lost 2 HP!", result2);
        assertEquals(6, player.getHealth());
    }
}