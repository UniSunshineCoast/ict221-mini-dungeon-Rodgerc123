package dungeon.engine;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for PotionCell behavior.
 */
public class PotionCellTest {

    @Test
    public void testPotionRestoresHPAndBecomesUsed() {
        Player player = new Player(0, 0);
        PotionCell potion = new PotionCell(1, 1);

        // Simulate partial damage first
        player.loseHP(4); // HP now 6
        assertEquals(6, player.getHealth());
        assertEquals('H', potion.getSymbol());

        // First interaction: healing
        String result1 = potion.onEnter(player);
        assertEquals("You drank a health potion and gained 4 HP.", result1);
        assertEquals(10, player.getHealth());  // should be capped at 10
        assertEquals('.', potion.getSymbol());

        // Second interaction: already used
        String result2 = potion.onEnter(player);
        assertEquals("An empty potion bottle lies here.", result2);
        assertEquals(10, player.getHealth());  // no further healing
    }
}