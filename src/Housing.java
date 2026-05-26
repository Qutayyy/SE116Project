package objectville.cells;

import java.util.EnumSet;
import java.util.Set;

public class Housing extends Zone {

    private static final Set<Utility> REQUIRED =
        EnumSet.of(Utility.ELECTRICITY, Utility.WATER, Utility.INTERNET);

    public Housing(int row, int col) {
        super(row, col);
    }

    @Override
    public char getSymbol() {
        return 'H';
    }
    @Override
    public Set<Utility> getRequiredUtilities() {
        return REQUIRED;
    }

    @Override
    protected int computeQualifiedLevel() {
       
        boolean l2 = hasService(Service.SECURITY)
                  && hasService(Service.HEALTH)
                  && hasService(Service.EDUCATION);
      
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
