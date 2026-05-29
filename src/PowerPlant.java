
public class PowerPlant extends UtilityProvider {
    public PowerPlant(int row, int col) {
        super(row, col); 
    }
    @Override
    public char getSymbol() { 
        return 'P'; 
    }
    @Override
    public UtilityConsumer.Utility getProducedUtility() {
        
        return UtilityConsumer.Utility.ELECTRICITY;
    }
}
