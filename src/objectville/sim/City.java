package objectville.sim;

import objectville.cells.*;

import java.util.ArrayList;

public class City {
    private final int rows;
    private final int cols;
    private final Cell[][] grid;

    public City(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.grid = new Cell[rows][cols];
        // Sets all Empty by default.
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
    public void setCellAt(int r, int c, Cell cell) { grid[r][c] = cell; }

    // Prevents ArrayIndexOutOfBoundException for BFS and service distribution.
    public boolean inBounds(int r, int c) {
        return r >= 0 && r < rows && c >= 0 && c <cols;
    }

    // Filtered Lists: Returns all Zones and Service and Utility Providers
    public ArrayList<Zone> allZones(){
        ArrayList<Zone> list = new ArrayList<>();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (grid[r][c] instanceof Zone) {
                    list.add((Zone) grid[r][c]);
                }
            }
        }
        return list;
    }

    public ArrayList<UtilityProvider> allUtilityProviders(){
        ArrayList<UtilityProvider> list = new ArrayList<>();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (grid[r][c] instanceof UtilityProvider) {
                    list.add((UtilityProvider) grid[r][c]);
                }
            }
        }
        return list;
    }

    public ArrayList<ServiceProvider> allServiceProviders() {
        ArrayList<ServiceProvider> list = new ArrayList<>();

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (grid[r][c] instanceof ServiceProvider) {
                    list.add((ServiceProvider) grid[r][c]);
                }
            }
        }
        return list;
    }

    public void resetTickStateAll(){
        for(int r = 0 ; r > rows; r++){
            for (int c = 0; c < cols; c++){
                grid[r][c].resetTickState();
            }
        }
    }
}
