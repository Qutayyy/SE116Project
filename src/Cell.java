public  abstract class Cell {
    private final int row;
    private final int col;

    protected Cell(int row, int col) {
        this.col = col;
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }

    public abstract char getSymbol();

    public boolean isConnectable() { return false; }

    public void resetTickState() {}
}
