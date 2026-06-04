package objectville.cells;

import java.util.EnumSet;
import java.util.Set;


public class Commercial extends Zone {

    private static final Set<UtilityConsumer.Utility> REQUIRED =
        EnumSet.of(UtilityConsumer.Utility.ELECTRICITY, UtilityConsumer.Utility.WATER, UtilityConsumer.Utility.INTERNET);

    public Commercial(int row, int col) {
      super(row, col); 
    }

    @Override
    public char getSymbol() { 
        return 'C'; 
    }
    @Override
    public Set<UtilityConsumer.Utility> getRequiredUtilities() {
        return REQUIRED;
    }

    @Override
    protected int computeQualifiedLevel() {
        if (getReceivedPopulation() <= 0 || getReceivedGoods() <= 0) return 0;
        boolean l2 = hasService(ServiceConsumer.Service.SECURITY);
        int m = minDeliveredAcrossRequired();
        boolean l3 = l2 && getReceivedPopulation() > m && getReceivedGoods() > m;
        if (l3) return 3;
        if (l2) return 2;
        return 1;
    }

    @Override
    protected int computeOutput(int m) {
        switch (getLevel()) {
            case 1: return m;
            case 2: return 2 * m;
            case 3: return 2 * m + Math.min(getReceivedPopulation(), getReceivedGoods());
            default: return 0;
        }
    }
}
