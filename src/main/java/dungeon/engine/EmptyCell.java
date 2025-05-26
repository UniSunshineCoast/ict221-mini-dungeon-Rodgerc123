package dungeon.engine;

public class EmptyCell extends GameCell {

    public EmptyCell(int row, int col) {
        super(row, col);
    }

    @Override
    public char getSymbol() {
        return ' ';  // ← instructs GameCellView to show background only
    }

    @Override
    public String onEnter(Player player) {
        return "You see nothing of interest.";
    }
}