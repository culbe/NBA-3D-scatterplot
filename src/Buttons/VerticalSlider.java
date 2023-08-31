package Buttons;
import java.awt.Graphics;

import Points.Point;

import java.awt.Color;

public class VerticalSlider extends Button<Double>{
    
    double min, max;

    public VerticalSlider(){
        value = 0.0;
        bottomRight = topLeft = null;
    }
    public VerticalSlider(Point topleft, Point bottomright){
        topLeft = topleft;
        bottomRight = bottomright;
        value = 0.0;
        min = 0;
        max = bottomright.getY()-topleft.getY();
    }
    public VerticalSlider(Point topleft, Point bottomright, double value){
        topLeft = topleft;
        bottomRight = bottomright;
        this.value = value;
        min = 0;
        max = bottomright.getY()-topleft.getY();
    }
    public VerticalSlider(Point topleft, Point bottomright, double min, double max){
        topLeft = topleft;
        bottomRight = bottomright;
        value = min;
        this.min = min;
        this.max = max;
    }
    public VerticalSlider(Point topleft, Point bottomright, double min, double max, double value){
        topLeft = topleft;
        bottomRight = bottomright;
        this.min = min;
        this.max = max;    
        this.value = value;
    }
        
    public double setValue(Point p){
        int travel = bottomRight.getY()-topLeft.getY();
        double range = max - min;
        int dy = bottomRight.getY() - p.getY();
        double v = 1.0*dy/travel*range+min;
        value = v;
        return v;
    }

    public void draw(Graphics g){
        int width = bottomRight.getX() - topLeft.getX();
        int height = bottomRight.getY() - topLeft.getY();
        double range = max - min;
        g.setColor(Color.WHITE);
        // g.drawLine(topLeft.getX() + width / 2, topLeft.getY(), topLeft.getX() + width / 2, bottomRight.getY());
        // g.drawLine(topLeft.getX() + width / 2 - 1, topLeft.getY(), topLeft.getX() + width / 2 - 1, bottomRight.getY());
        // g.drawLine(topLeft.getX() + width / 2 + 1, topLeft.getY(), topLeft.getX() + width / 2 + 1, bottomRight.getY());
        g.fillRect(topLeft.getX() + width / 2-1, topLeft.getY(), 3, bottomRight.getY()-topLeft.getY());
        g.setColor(Color.GRAY);
        g.fillOval(topLeft.getX(), (int) (bottomRight.getY() - width / 2 - ((value-min) / range * height)), width,width);
    }
}
