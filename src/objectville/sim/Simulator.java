package objectville.sim;

import objectville.cells.*;

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

}
