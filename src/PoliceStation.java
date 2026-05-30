public class PoliceStation extends ServiceProvider {
    public PoliceStation(int row, int col){
        super(row, col, 5);
    }

    @Override
    public char getSymbol(){
        return 'F';
    }
    @Override
    public String getServiceType(){
        return "security"; }
}
