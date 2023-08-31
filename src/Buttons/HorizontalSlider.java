package Buttons;
import java.awt.Graphics;

import Points.Point;

import java.awt.Color;

public class HorizontalSlider extends Button<Double>{
    
    double min, max;

    public HorizontalSlider(){
        value = 0.0;
        bottomRight = topLeft = null;
    }
    public HorizontalSlider(Point topleft, Point bottomright){
        topLeft = topleft;
        bottomRight = bottomright;
        value = 0.0;
        min = 0;
        max = bottomright.getX()-topleft.getX();
    }
    public HorizontalSlider(Point topleft, Point bottomright, double value){
        topLeft = topleft;
        bottomRight = bottomright;
        this.value = value;
        min = 0;
        max = bottomright.getX()-topleft.getX();
    }
    public HorizontalSlider(Point topleft, Point bottomright, double min, double max){
        topLeft = topleft;
        bottomRight = bottomright;
        value = min;
        this.min = min;
        this.max = max;
    }
    public HorizontalSlider(Point topleft, Point bottomright, double min, double max, double value){
        topLeft = topleft;
        bottomRight = bottomright;
        this.min = min;
        this.max = max;    
        this.value = value;
    }
        
    public double setValue(Point p){
        int travel = bottomRight.getX()-topLeft.getX();
        double range = max - min;
        int dx = p.getX()-topLeft.getX();
        double v = Math.min(1.0*dx/travel*range+min, max);
        v = Math.max(v, min);
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
        g.fillRect( topLeft.getX(), topLeft.getY() + height/2 - 1, bottomRight.getX()-topLeft.getX(), 3);
        g.setColor(Color.LIGHT_GRAY);
        g.fillOval((int) (bottomRight.getX() - height / 2 - ((max-value) / range * width)), topLeft.getY(), height,height);
        g.setColor(Color.BLACK);
        g.drawString(""+value.intValue(), (int)(bottomRight.getX() - height / 2 - ((max-value) / range * width))+1, topLeft.getY() + 15);
    }
}
