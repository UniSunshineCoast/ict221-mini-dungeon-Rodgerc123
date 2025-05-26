package dungeon.engine;

public class WallCell extends GameCell {
    private static final long serialVersionUID = 1L;

    public WallCell(int row, int col) {
        super(row, col);
    }

    @Override
    public char getSymbol() {
        return '#';
    }

    @Override
    public String onEnter(Player player) {
        return "You bump into a wall. Ouch!";
    }
}
