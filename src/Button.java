public abstract class Button<E> {
    E value;
    Point topLeft, bottomRight;

    public boolean contains(Point p){
        if(p.X<topLeft.X || p.X>bottomRight.X){
            return false;
        }
        if(p.Y<topLeft.Y || p.Y>bottomRight.Y){
            return false;
        }
        return true;
    }

    //returns previous value
    public void setValue(E val){
        value = val;
    }
}
