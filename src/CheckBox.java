// import java.awt.Graphics;
// import java.awt.Graphics2D;
// import java.awt.Color;
import java.awt.*;

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
        int width = bottomRight.X - topLeft.X;
        int height = bottomRight.Y - topLeft.Y;
        g.setColor(Color.WHITE);
        g.drawRect(topLeft.X, topLeft.Y, width, height);
        if (value) {
            g.setColor(Color.RED);
            g.drawLine(topLeft.X+2, topLeft.Y+2, topLeft.X+width-2, topLeft.Y+height-2);
            g.drawLine(topLeft.X+width-2, topLeft.Y+2, topLeft.X+2, topLeft.Y+height-2);
        }
        // if (value) {
        //     g.setColor(Color.GREEN);
        //     g.drawLine(topLeft.X + width / 4, topLeft.Y + height / 2, topLeft.X + width / 2, bottomRight.Y);
        //     g.drawLine(topLeft.X + width / 2, bottomRight.Y, bottomRight.X + width / 4, topLeft.Y - height / 4);
        //     g.drawLine(topLeft.X + width / 4 + 1, topLeft.Y + height / 2, topLeft.X + width / 2 + 1,
        //             bottomRight.Y);
        //     g.drawLine(topLeft.X + width / 2 + 1, bottomRight.Y, bottomRight.X + width / 4 + 1, topLeft.Y - height / 4);
        // }
    }

}
