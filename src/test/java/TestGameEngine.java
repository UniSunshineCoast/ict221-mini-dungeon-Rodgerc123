import dungeon.engine.GameEngine;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestGameEngine {

    @Test
    void testGetSize() {
        // Updated to match the new constructor: (size, difficulty, playerName)
        GameEngine ge = new GameEngine(10, 3, "Tester");

        assertEquals(10, ge.getSize());
    }
}