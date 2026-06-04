package objectville.cells;

public class Hospital extends ServiceProvider {
    public Hospital(int row, int col){
        super(row, col, 3);
    }

    @Override
    public char getSymbol(){
        return 'D';
    }
    @Override
    public ServiceConsumer.Service getServiceType(){return ServiceConsumer.Service.HEALTH;}
}
