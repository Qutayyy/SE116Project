

public abstract class UtilityProvider extends Cell {

    private static final int CAPACITY = 100;

    protected UtilityProvider(int row, int col) {
        super(row, col);
    }

    public abstract UtilityConsumer.Utility getProducedUtility();

    public int getCapacity() { 
        return CAPACITY; 
    }
}
