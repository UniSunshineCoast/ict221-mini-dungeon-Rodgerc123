package dungeon.engine;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for GoldCell interaction with Player.
 */
public class GoldCellTest {

    @Test
    public void testGoldCellIncreasesScoreAndRemovesGold() {
        Player player = new Player(0, 0, "TestPlayer");
        GoldCell goldCell = new GoldCell(0, 1);

        // Initial assertions
        assertEquals(0, player.getScore());
        assertEquals('G', goldCell.getSymbol());

        // Simulate stepping on gold
        String result = goldCell.onEnter(player);

        // Assert expected effects
        assertEquals("You picked up a gold.", result);
        assertEquals(2, player.getScore());
        assertEquals('.', goldCell.getSymbol());
    }
}
