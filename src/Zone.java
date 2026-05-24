package objectville.cells;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

public abstract class Zone extends Cell implements UtilityConsumer, ServiceConsumer {


    private int level = 0;
  
    private int currentOutput = 0
  
    private final Map<Utility, Integer> receivedUtilities = new EnumMap<>(Utility.class);

    private final Set<Service> receivedServices = EnumSet.noneOf(Service.class);


    private int receivedPopulation = 0;
    private int receivedGoods = 0;
    private int receivedLifestyle = 0;

    protected Zone(int row, int col) {
        super(row, col);
        for (Utility u : Utility.values()) receivedUtilities.put(u, 0);
    }

    @Override 
  public boolean isConnectable() { 
      return true;
    }

    public int getLevel() {
      return level;
    }
    public int getCurrentOutput() {
      return currentOutput; 
    }

    protected int getReceivedPopulation() {
      return receivedPopulation; 
    }
    protected int getReceivedGoods() { 
      return receivedGoods;
    }
    protected int getReceivedLifestyle() {
      return receivedLifestyle; 
    }



    public abstract Set<Utility> getRequiredUtilities();

  
    @Override
    public int demandFor(Utility u) {
        if (!getRequiredUtilities().contains(u)) return 0;
        int totalDemand = Math.max(1, currentOutput);
        int alreadyReceived = receivedUtilities.get(u);
        return Math.max(0, totalDemand - alreadyReceived);
    }

    @Override
    public void receiveUtility(Utility u, int amount) {
        if (amount <= 0) return;
        receivedUtilities.merge(u, amount, Integer::sum);
    }


    @Override 
  public void receiveService(Service s) {
      receivedServices.add(s); }
    @Override
  public boolean hasService(Service s) {
      return receivedServices.contains(s); }


    public void receivePopulation(int amount) {
      receivedPopulation += amount; 
    }
    public void receiveGoods(int amount) {
      receivedGoods += amount; 
    }
    public void receiveLifestyle(int amount) {
      receivedLifestyle += amount;
    }


    @Override
    public void resetTickState() {
        for (Utility u : Utility.values()) receivedUtilities.put(u, 0);
        receivedServices.clear();
        receivedPopulation = 0;
        receivedGoods = 0;
        receivedLifestyle = 0;
    }

    public int minDeliveredAcrossRequired() {
        Set<Utility> req = getRequiredUtilities();
        if (req.isEmpty()) return 0;
        int m = Integer.MAX_VALUE;
        for (Utility u : req) {
            int got = receivedUtilities.get(u);
            if (got <= 0) return -1;
            if (got < m) m = got;
        }
        return m;
    }

  
    public void updateLevel() {
        if (minDeliveredAcrossRequired() == -1) {
            level = 0;
            return;
        }
        int qualified = computeQualifiedLevel();
        if (qualified > level) level = level + 1;
        else if (qualified < level) level = level - 1;
      
        if (level < 0) level = 0;
        if (level > 3) level = 3;
    }

    protected abstract int computeQualifiedLevel();


    public void computeAndStoreOutput() {
        int m = minDeliveredAcrossRequired();
        if (m < 0 || level == 0) { currentOutput = 0; return; }
        currentOutput = computeOutput(m);
    }

    protected abstract int computeOutput(int m);
}
