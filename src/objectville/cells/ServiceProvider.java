package objectville.cells;

public abstract class ServiceProvider extends Cell {
private int radius;

    public ServiceProvider(int row, int col, int radius) {
        super(row, col);
        this.radius = radius;
    }
 public int getRadius() {
return radius; 
}
public abstract ServiceConsumer.Service getServiceType();
}


