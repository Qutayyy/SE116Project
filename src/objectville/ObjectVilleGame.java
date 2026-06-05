package objectville;

import objectville.io.MapLoader;
import objectville.sim.City;
import objectville.sim.Simulator;

// Usage: java -jar ObjectVilleGame.jar mymap.txt 5
public class ObjectVilleGame {
    public static void main(String[] args) {
        if (args.length < 2){
            System.err.println("Usage: java -jar ObjectVilleGame.jar <map-file> <ticks>");
            System.exit(1);
        }

        String mapPath = args[0];
        int ticks;
        try {
            ticks = Integer.parseInt(args[1]);
        } catch (RuntimeException e) {
            System.err.println("Error: Tick count must be a non-negative integer");
            System.exit(1);
            return;
        }

        try {
            City city = MapLoader.loadFromFile(mapPath);
            Simulator sim = new Simulator(city);

            for (int t = 1; t <= ticks; t++) {
                sim.runTick(t);
            }
        } catch (Exception ex) {
            System.err.println("Error: " + ex.getMessage());
            System.exit(2);
        }
    }
}
