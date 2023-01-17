// import java.awt.Graphics;
// import java.awt.Graphics2D;
// import java.awt.Color;
import java.awt.*;

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
        int width = bottomRight.X - topLeft.X;
        int height = bottomRight.Y - topLeft.Y;
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(topLeft.X, topLeft.Y, width, height);
        g.setColor(Color.BLACK);
        g.drawString(value, topLeft.X+10, bottomRight.Y-10);
    }

}
