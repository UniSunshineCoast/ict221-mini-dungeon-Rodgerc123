package dungeon.engine;

import java.io.Serializable;

public class LadderCell extends GameCell implements Serializable {
    private static final long serialVersionUID = 1L;

    public LadderCell(int row, int col) {
        super(row, col);
    }

    @Override
    public char getSymbol() {
        return 'L'; // Ladder symbol
    }

    @Override
    public String onEnter(Player player) {
        return "You found the ladder!";
    }
}