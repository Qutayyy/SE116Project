package objectville.cells;

import java.util.EnumSet;
import java.util.Set;

public class Housing extends Zone {

    private static final Set<UtilityConsumer.Utility> REQUIRED =
        EnumSet.of(UtilityConsumer.Utility.ELECTRICITY, UtilityConsumer.Utility.WATER, UtilityConsumer.Utility.INTERNET);

    public Housing(int row, int col) {
        super(row, col);
    }

    @Override
    public char getSymbol() {
        return 'H';
    }
    @Override
    public Set<UtilityConsumer.Utility> getRequiredUtilities() {
        return REQUIRED;
    }

    @Override
    protected int computeQualifiedLevel() {
       
        boolean l2 = hasService(ServiceConsumer.Service.SECURITY)
                  && hasService(ServiceConsumer.Service.HEALTH)
                  && hasService(ServiceConsumer.Service.EDUCATION);
      
        boolean l3 = l2 && getReceivedLifestyle() > 0;
        if (l3) return 3;
        if (l2) return 2;
        return 1;
    }

    @Override
    protected int computeOutput(int m) {
        switch (getLevel()) {
            case 1: return m;
            case 2: return 2 * m;
            case 3: return 2 * m + getReceivedLifestyle();
            default: return 0;
        }
    }
}
