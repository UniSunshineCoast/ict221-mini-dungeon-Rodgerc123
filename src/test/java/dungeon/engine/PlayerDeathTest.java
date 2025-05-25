package dungeon.engine;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for verifying player death condition.
 */
public class PlayerDeathTest {

    @Test
    public void testPlayerDeathCondition() {
        Player player = new Player(0, 0);

        assertTrue(player.isAlive());
        assertEquals(10, player.getHealth());

        // Bring HP down in chunks
        player.loseHP(3);
        assertTrue(player.isAlive());
        assertEquals(7, player.getHealth());

        player.loseHP(6);
        assertTrue(player.isAlive());
        assertEquals(1, player.getHealth());

        // Final blow
        player.loseHP(1);
        assertEquals(0, player.getHealth());
        assertFalse(player.isAlive());

        // Overkill (just in case)
        player.loseHP(2);
        assertTrue(player.getHealth() < 0);
        assertFalse(player.isAlive());
    }
}