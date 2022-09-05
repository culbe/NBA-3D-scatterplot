public class Point {
    
    int X, Y;

    public Point(){
        X = Y = 0;
    }

    public Point(int x, int y){
        X = x;
        Y = y;
    }

    public String toString(){
        return "X: " + X + " Y: " + Y;
    }
}
