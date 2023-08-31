package Buttons;

import Points.Point;

public abstract class Button<E> {
    E value;
    Point topLeft, bottomRight;

    public boolean contains(Point p){
        if(p.getX()<topLeft.getX() || p.getX()>bottomRight.getX()){
            return false;
        }
        if(p.getY()<topLeft.getY() || p.getY()>bottomRight.getY()){
            return false;
        }
        return true;
    }

    public void setValue(E val){
        value = val;
    }

    public E getValue(){
        return value;
    }

    public Point getTopLeft(){
        return topLeft;
    }

    public void setTopLeft(Point p){
        topLeft = p;
    }

    public Point getBottomRight(){
        return bottomRight;
    }

    public void setBottomRight(Point p){
        bottomRight = p;
    }

}
