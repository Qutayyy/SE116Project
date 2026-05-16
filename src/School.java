public class School extends Service {
    public School(int row, int col) {
        super(row, col, 4);
    }

    @Override
    public char getSymbol() {
        return 'S';
    }
    @Override
    public String getServiceType() {
        return "education";
    }
}
