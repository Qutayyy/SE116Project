abstract class Service extends Cell{
private int radius;

    public Service(int row, int col, int radius) {
        super(row, col);
        this.radius = radius;
    }
 public int getRadius() {
return radius; 
}
public abstract ServiceConsumer.Service getServiceType();
}


