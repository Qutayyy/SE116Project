/* We used Manhattan distance because we only move in 4 direction,
 and it is easier to calculate since there are no square root math.*/
public class Distance {
    private Distance (){}

    public static int manhattan (int r1, int c1, int r2, int c2){
        return Math.abs(r1 - r2) + Math.abs(c1 - c2);
    }
}
