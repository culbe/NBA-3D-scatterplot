import java.awt.Graphics;
import java.awt.Color;

public class Dragger extends Button<Integer>{


    public Dragger(Point p, int size){
        value = size;
        topLeft = p;
        bottomRight = new Point(p.X+value, p.Y+value);
    }

    public void draw(Graphics g){
        g.setColor(Color.RED);
        g.fillOval(topLeft.X-value/2, topLeft.Y-value/2, value, value);
    }

    public boolean contains(Point p){
        if(p.X<topLeft.X-value/2 || p.X>bottomRight.X-value/2){
            return false;
        }
        if(p.Y<topLeft.Y-value/2 || p.Y>bottomRight.Y-value/2){
            return false;
        }
        return true;
    }
}
