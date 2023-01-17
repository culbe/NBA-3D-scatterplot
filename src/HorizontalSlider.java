import java.awt.Graphics;
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
        max = bottomright.X-topleft.X;
    }
    public HorizontalSlider(Point topleft, Point bottomright, double value){
        topLeft = topleft;
        bottomRight = bottomright;
        this.value = value;
        min = 0;
        max = bottomright.X-topleft.X;
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
        int travel = bottomRight.X-topLeft.X;
        double range = max - min;
        int dx = p.X-topLeft.X;
        double v = Math.min(1.0*dx/travel*range+min, max);
        v = Math.max(v, min);
        value = v;
        return v;
    }

    public void draw(Graphics g){
        int width = bottomRight.X - topLeft.X;
        int height = bottomRight.Y - topLeft.Y;
        double range = max - min;
        g.setColor(Color.WHITE);
        // g.drawLine(topLeft.X + width / 2, topLeft.Y, topLeft.X + width / 2, bottomRight.Y);
        // g.drawLine(topLeft.X + width / 2 - 1, topLeft.Y, topLeft.X + width / 2 - 1, bottomRight.Y);
        // g.drawLine(topLeft.X + width / 2 + 1, topLeft.Y, topLeft.X + width / 2 + 1, bottomRight.Y);
        g.fillRect( topLeft.X, topLeft.Y + height/2 - 1, bottomRight.X-topLeft.X, 3);
        g.setColor(Color.LIGHT_GRAY);
        g.fillOval((int) (bottomRight.X - height / 2 - ((max-value) / range * width)), topLeft.Y, height,height);
        g.setColor(Color.BLACK);
        g.drawString(""+value.intValue(), (int)(bottomRight.X - height / 2 - ((max-value) / range * width))+1, topLeft.Y + 15);
    }
}
