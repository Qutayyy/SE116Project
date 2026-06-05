package objectville.sim;

import objectville.cells.*;
import objectville.cells.UtilityConsumer.Utility;

import java.util.ArrayDeque;
import java.util.Deque;

public class Simulator {
    private final City city;
    private int prevTotalPopulation = 0;
    private int prevTotalGoods = 0;
    private int prevTotalLifestyle = 0;

    public Simulator(City city) {
        this.city = city;
    }

    public void runTick(){
       city.resetTickStateAll();

    }

    // Step 1: Service Distribution
    private void step1_distributeServices(){
        for (ServiceProvider sp : city.allServiceProviders()){
            int radius = sp.getRadius();
            int sr = sp.getRow();
            int sc = sp.getCol();

            for (int dr = -radius; dr <= radius; dr++){
                for (int dc = -radius; dc <= radius; dc++){
                    int r = sr + dr;
                    int c = sc + dc;
                    if (!city.inBounds(r,c)){
                        continue;
                    }
                    if (Distance.manhattan(sr, sc, r, c) > radius){
                        continue;
                    }

                    Cell cell = city.getCellAt(r,c);
                    if(cell instanceof ServiceConsumer){
                        ((ServiceConsumer) cell).receiveService(sp.getServiceType());
                    }
                }
            }
        }
    }
    //Step 2 : BFS
    private void bfsDistribute(UtilityProvider provider){
        int rows = city.getRows();
        int cols = city.getCols();
        boolean[][] visited = new boolean[rows][cols];

        Utility utility = provider.getProducedUtility();
        int budget = provider.getCapacity();

        Deque<Position> queue = new ArrayDeque<>();

        // Add only the starting provider to the queue
        visited[provider.getRow()][provider.getCol()] = true;
        queue.add(new Position(provider.getRow(), provider.getCol()));

        // 8 neighborhood directions
        int[][] directions = {
                {-1, -1}, {-1, 0}, {-1, 1},
                {0, -1},           {0, 1},
                {1, -1},  {1, 0},  {1, 1}
        };

        while (!queue.isEmpty()) {
            Position current = queue.poll();
            int r = current.getRow();
            int c = current.getCol();
            Cell cell = city.getCellAt(r, c);

            if (cell instanceof UtilityConsumer){
                UtilityConsumer consumer = (UtilityConsumer) cell;
                int demand = consumer.demandFor(utility);
                int give = Math.min(demand, budget);

                if (give > 0){
                    consumer.receiveUtility(utility, give);
                    budget -= give;
                }
            }

            if (budget <= 0) break;

            for (int[] dir : directions){
                int newRow = r + dir[0];
                int newCol = c + dir[1];

                if (!city.inBounds(newRow, newCol)) continue;
                if (visited[newRow][newCol]) continue;
                if (!city.getCellAt(newRow, newCol).isConnectable()) continue;

                visited[newRow][newCol] = true;
                queue.add(new Position(newRow, newCol));
            }
        }
    }


}
