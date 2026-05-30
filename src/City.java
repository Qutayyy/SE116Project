public class City {
    private final int rows;
    private final int cols;
    private final Cell[][] grid;

    public City(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.grid = new Cell[rows][cols];
        for (int r =0; r < rows; r++){
            for (int c = 0; c < cols; c++){
                grid[r][c] = new EmptyCell(r, c);
            }
        }
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }
    public Cell getCellAt(int r, int c){ return grid[r][c]; }
    public void setCellA(int r, int c, Cell cell) { grid[r][c] = cell; }

    // Prevents ArrayIndexOutOfBoundException for BFS and service distribution.
    public boolean inBounds(int r, int c) {
        return r >= 0 && r < rows && c >= 0 && c <cols;
    }

}
