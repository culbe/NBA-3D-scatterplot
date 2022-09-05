import java.awt.Graphics;
import java.awt.Color;

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
            g.setColor(Color.GREEN);
            g.drawLine(topLeft.X + width / 4, topLeft.Y + height / 2, topLeft.X + width / 2, bottomRight.Y);
            g.drawLine(topLeft.X + width / 2, bottomRight.Y, bottomRight.X + width / 4, topLeft.Y - height / 4);
            g.drawLine(topLeft.X + width / 4 + 1, topLeft.Y + height / 2, topLeft.X + width / 2 + 1,
                    bottomRight.Y);
            g.drawLine(topLeft.X + width / 2 + 1, bottomRight.Y, bottomRight.X + width / 4 + 1, topLeft.Y - height / 4);
        }
    }

    public void drawPlain(Graphics g) {
        int width = bottomRight.X - topLeft.X;
        int height = bottomRight.Y - topLeft.Y;
        g.setColor(Color.LIGHT_GRAY);
        if(value)
            g.setColor(Color.DARK_GRAY);
        g.fillRect(topLeft.X, topLeft.Y, width, height);
    }

}
