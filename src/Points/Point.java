package Points;
public class Point {
    
    int X, Y;

    public Point(){
        X = Y = 0;
    }

    public Point(int x, int y){
        X = x;
        Y = y;
    }

    public void setX(int x){
        X = x;
    }

    public void setY(int y){
        Y = y;
    }

    public int getX(){
        return X;
    }

    public int getY(){
        return Y;
    }

    public String toString(){
        return "X: " + X + " Y: " + Y;
    }
}
