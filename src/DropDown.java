import java.awt.Graphics;
import java.awt.Color;

public class DropDown extends Button<Integer>{

    int itemCount;

    public DropDown(Point topleft, Point bottomright, int count){
        topLeft = topleft;
        bottomRight = bottomright;
        itemCount = count;
        value = -1;
    }

    //returns value after toggle is complete
    public int toggle(){
        value *=-1;
        if(value>0){
            bottomRight.Y += (bottomRight.Y-topLeft.Y)*itemCount;
        }else{
            bottomRight.Y -= (bottomRight.Y-topLeft.Y)/(itemCount+1)*(itemCount);
        }
        return value;
    }

    public void close(){
        if(value>0){
            value *=-1;
            bottomRight.Y -= (bottomRight.Y-topLeft.Y)/(itemCount+1)*(itemCount);
        }
        return;
    }

    public void draw(Graphics g) {
        int width = bottomRight.X - topLeft.X;
        int height = bottomRight.Y - topLeft.Y;
        g.setColor(Color.WHITE);
        g.fillRect(topLeft.X, topLeft.Y, width, height);
        if (value>0) {
            height = (bottomRight.Y-topLeft.Y)/(itemCount+1);
            for (int i = 1; i <= itemCount; i++) {
                g.setColor(Color.BLACK);
                g.drawRect(topLeft.X, topLeft.Y+i*height, width, height);
            }
        }
    }

    public void drawColor(Graphics g, int[][][] colorArr) {
        int width = bottomRight.X - topLeft.X;
        int height = bottomRight.Y - topLeft.Y;
        g.setColor(Color.WHITE);
        g.fillRect(topLeft.X, topLeft.Y, width, height);
        if (value>0) {
            height = (bottomRight.Y-topLeft.Y)/(itemCount+1);
            width/=5;
            for (int i = 0; i < itemCount; i++) {
                for (int j = 0; j < 5; j++) {
                    g.setColor(new Color(colorArr[i][j+1][0], colorArr[i][j+1][1], colorArr[i][j+1][2]));
                    g.fillRect(topLeft.X+j*width, topLeft.Y+(i+1)*height, width, height);
                }
            }
        }
    }

    public int adjust(Point p){
        if(this.contains(p)){
            if(value<0){
                toggle();
                return value;
            }
            int val = (int)((p.Y-topLeft.Y)/((bottomRight.Y-topLeft.Y)/(itemCount+1)));
            if(val == 0){
                toggle();
                return value;
            }else{
                value = val;
                return value;
            }
        }
        close();
        return value; 
    }

}
