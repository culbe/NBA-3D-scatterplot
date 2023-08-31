package Buttons;
// import java.awt.Graphics;
// import java.awt.Graphics2D;
// import java.awt.Color;
import java.awt.*;

import Points.Point;

public class SimpleButton extends Button<String>{

    public SimpleButton(){
        value = "";
        bottomRight = topLeft = null;
    }

    public SimpleButton(Point topleft, Point bottomright){
        topLeft = topleft;
        bottomRight = bottomright;
        value = "";
    }
    public SimpleButton(Point topleft, Point bottomright, String value){
        topLeft = topleft;
        bottomRight = bottomright;
        this.value = value;
    }

    public void draw(Graphics g) {
        int width = bottomRight.getX() - topLeft.getX();
        int height = bottomRight.getY() - topLeft.getY();
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(topLeft.getX(), topLeft.getY(), width, height);
        g.setColor(Color.BLACK);
        g.drawString(value, topLeft.getX()+10, bottomRight.getY()-10);
    }

}
