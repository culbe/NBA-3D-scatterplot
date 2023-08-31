package Buttons;
// import java.awt.Graphics;
// import java.awt.Graphics2D;
// import java.awt.Color;
import java.awt.*;

import Points.Point;

public class CheckBox extends Button<Boolean>{

    public CheckBox(){
        value = false;
        bottomRight = topLeft = null;
    }

    public CheckBox(Point topleft, Point bottomright){
        topLeft = topleft;
        bottomRight = bottomright;
        value = false;
    }
    public CheckBox(Point topleft, Point bottomright, boolean value){
        topLeft = topleft;
        bottomRight = bottomright;
        this.value = value;
    }

    //returns value after toggle is complete
    public boolean toggle(){
        value = !value;
        return value;
    }

    public void draw(Graphics g) {
        int width = bottomRight.getX() - topLeft.getX();
        int height = bottomRight.getY() - topLeft.getY();
        g.setColor(Color.WHITE);
        g.drawRect(topLeft.getX(), topLeft.getY(), width, height);
        if (value) {
            g.setColor(Color.RED);
            g.drawLine(topLeft.getX()+2, topLeft.getY()+2, topLeft.getX()+width-2, topLeft.getY()+height-2);
            g.drawLine(topLeft.getX()+width-2, topLeft.getY()+2, topLeft.getX()+2, topLeft.getY()+height-2);
        }
    }

}
