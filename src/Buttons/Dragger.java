package Buttons;
import java.awt.Graphics;

import Points.Point;

import java.awt.Color;

public class Dragger extends Button<Integer>{


    public Dragger(Point p, int size){
        value = size;
        topLeft = p;
        bottomRight = new Point(p.getX()+value, p.getY()+value);
    }

    public void draw(Graphics g){
        g.setColor(Color.RED);
        g.fillOval(topLeft.getX()-value/2, topLeft.getY()-value/2, value, value);
    }

    public boolean contains(Point p){
        if(p.getX()<topLeft.getX()-value/2 || p.getX()>bottomRight.getX()-value/2){
            return false;
        }
        if(p.getY()<topLeft.getY()-value/2 || p.getY()>bottomRight.getY()-value/2){
            return false;
        }
        return true;
    }
}
