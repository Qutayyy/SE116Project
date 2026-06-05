package objectville.sim;

import objectville.cells.*;
import objectville.cells.UtilityConsumer.Utility;
import static objectville.cells.ServiceConsumer.Service;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;

public class Simulator {
    private final City city;
    private int prevTotalPopulation = 0;
    private int prevTotalGoods = 0;
    private int prevTotalLifestyle = 0;

    public Simulator(City city) {
        this.city = city;
    }

    public void runTick(int tickNumber){
        System.out.println("Tick " + tickNumber);
        city.resetTickStateAll();
        step1_distributeServices();
        step2_distributeUtilities();
        step3_distributeResources();
        step4_updateZones();
        step5_accumulateProduction();

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
                    if (cell instanceof Zone) {
                        Zone z = (Zone) cell;
                        Service serviceType = sp.getServiceType();

                        boolean applicable = false;
                        if (z instanceof Housing) applicable = true;
                        else if (z instanceof Industrial) applicable = (serviceType == ServiceConsumer.Service.SECURITY);
                        else if (z instanceof Commercial) applicable = (serviceType == ServiceConsumer.Service.SECURITY);

                        if (applicable) {
                            z.receiveService(serviceType);
                            System.out.println(z.getDisplayName() + " at (" + r + "," + c
                                    + ") received " + serviceName(serviceType) + " service");
                        }
                    }
                }
            }
        }
    }
    //Step 2 : BFS

    private void step2_distributeUtilities() {
        for (UtilityProvider up : city.allUtilityProviders()) {
            bfsDistribute(up);
        }
    }

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
                    System.out.println(cell.getDisplayName() + " at (" + r + "," + c
                            + ") received " + give + " " + utilityName(utility));
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

    //Step 3 : Product Distribution

    private void step3_distributeResources() {
        ArrayList<Zone> zones = city.allZones();

        int houseCount = 0, indCount = 0, comCount = 0;
        for (Zone z : zones) {
            if (z instanceof Housing) houseCount++;
            else if (z instanceof Industrial) indCount++;
            else if (z instanceof Commercial) comCount++;
        }

        //Population --> industrial and commercial
        int popTargets = indCount + comCount;
        int popPer = (popTargets > 0) ? (prevTotalPopulation / popTargets) : 0;

        //Goods --> commercial
        int goodsPer = (comCount > 0) ? (prevTotalGoods / comCount) : 0;

        //Lifestyle --> housing
        int lifePer = (houseCount > 0) ? (prevTotalLifestyle / houseCount) : 0;

        for (Zone z : zones) {
            if (z instanceof Housing) {
                z.receiveLifestyle(lifePer);
            } else if (z instanceof Industrial) {
                z.receivePopulation(popPer);
            } else if (z instanceof Commercial) {
                z.receivePopulation(popPer);
                z.receiveGoods(goodsPer);
            }
        }
    }

    //Step 4 : Update Zones
    private void step4_updateZones(){
        for (Zone z : city.allZones()) {
            z.updateLevel();
            z.computeAndStoreOutput();

            int r = z.getRow();
            int c = z.getCol();
            String name = z.getDisplayName();
            int output = z.getCurrentOutput();
            int oldLevel = z.getPreviousLevel();
            int newLevel = z.getLevel();

            String outputType;
            if (z instanceof Housing)         outputType = "population";
            else if (z instanceof Industrial) outputType = "goods";
            else                              outputType = "lifestyle";

            System.out.println(name + " at (" + r + "," + c + ") generated " + output + " " + outputType);

            if (newLevel > oldLevel) {
                System.out.println(name + " at (" + r + "," + c + ") levels up from " + oldLevel + " to " + newLevel);
            } else if (newLevel < oldLevel) {
                System.out.println(name + " at (" + r + "," + c + ") levels down from " + oldLevel + " to " + newLevel);
            }
        }
    }

    //Step 5 Accumulate Production
    private void step5_accumulateProduction(){
        int totPop = 0;
        int totGoods = 0;
        int totLife = 0;

        for (Zone z : city.allZones()){
            int out = z.getCurrentOutput();
            if (z instanceof Housing) totPop += out;
            else if (z instanceof  Industrial)totGoods += out;
            else if (z instanceof  Commercial)totLife += out;
        }
        prevTotalGoods = totGoods;
        prevTotalPopulation = totPop;
        prevTotalLifestyle = totLife;
    }

    private static String serviceName(Service s) {
        switch (s) {
            case SECURITY: return "security";
            case HEALTH: return "health";
            case EDUCATION: return "education";
            default: return s.name().toLowerCase();
        }
    }

    private static String utilityName(Utility u) {
        switch (u) {
            case ELECTRICITY: return "electricity";
            case WATER: return "water";
            case INTERNET: return "internet";
            default: return u.name().toLowerCase();
        }
    }
}
