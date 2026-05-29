public class InternetHub extends UtilityProvider {
    public InternetHub(int row, int col) {
        super(row, col); 
    }
    @Override 
    public char getSymbol() {
        return 'T'; 
    }
    @Override
    public UtilityConsumer.Utility getProducedUtility() {
        
        return UtilityConsumer.Utility.INTERNET;
    }
}
