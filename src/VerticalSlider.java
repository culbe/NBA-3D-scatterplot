import java.awt.Graphics;
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
        max = bottomright.Y-topleft.Y;
    }
    public VerticalSlider(Point topleft, Point bottomright, double value){
        topLeft = topleft;
        bottomRight = bottomright;
        this.value = value;
        min = 0;
        max = bottomright.Y-topleft.Y;
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
        int travel = bottomRight.Y-topLeft.Y;
        double range = max - min;
        int dy = bottomRight.Y - p.Y;
        double v = 1.0*dy/travel*range+min;
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
        g.fillRect(topLeft.X + width / 2-1, topLeft.Y, 3, bottomRight.Y-topLeft.Y);
        g.setColor(Color.GRAY);
        g.fillOval(topLeft.X, (int) (bottomRight.Y - width / 2 - ((value-min) / range * height)), width,width);
    }
}
