package Buttons;
import java.awt.Graphics;

import Points.Point;

import java.awt.Color;

public class DropDown extends Button<Integer>{

    //inherited Integer value
    //abs(value) stores which option is selected, starting from 1
    //if value<0, menu is closed
    int itemCount;

    public DropDown(Point topleft, Point bottomright, int count){
        topLeft = topleft;
        bottomRight = bottomright;
        itemCount = count;
        value = -1;
    }


    
    /***
     * Switches between open and closed menu
     * @return value after toggle is complete
     */
    public int toggle(){
        value *=-1;
        //change the dimensions so contains() can be used
        if(value>0){
            bottomRight.setY(bottomRight.getY() + (bottomRight.getY()-topLeft.getY())*itemCount);
        }else{
            bottomRight.setY(bottomRight.getY() - (bottomRight.getY()-topLeft.getY())/(itemCount+1)*(itemCount));
        }
        return value;
    }

    //Closes menu no matter what it is on
    public void close(){
        if(value>0){
            value *=-1;
            bottomRight.setY(bottomRight.getY() - (bottomRight.getY()-topLeft.getY())/(itemCount+1)*(itemCount));
        }
        return;
    }

    public void draw(Graphics g) {
        int width = bottomRight.getX() - topLeft.getX();
        int height = bottomRight.getY() - topLeft.getY();
        g.setColor(Color.WHITE);
        g.fillRect(topLeft.getX(), topLeft.getY(), width, height);
        if (value>0) {
            height = (bottomRight.getY()-topLeft.getY())/(itemCount+1);
            for (int i = 1; i <= itemCount; i++) {
                g.setColor(Color.BLACK);
                g.drawRect(topLeft.getX(), topLeft.getY()+i*height, width, height);
            }
        }
    }

    public void drawColor(Graphics g, int[][][] colorArr) {
        int width = bottomRight.getX() - topLeft.getX();
        int height = bottomRight.getY() - topLeft.getY();
        g.setColor(Color.WHITE);
        g.fillRect(topLeft.getX(), topLeft.getY(), width, height);
        if (value>0) {
            height = (bottomRight.getY()-topLeft.getY())/(itemCount+1);
            width/=5;
            for (int i = 0; i < itemCount; i++) {
                for (int j = 0; j < 5; j++) {
                    g.setColor(new Color(colorArr[i][j+1][0], colorArr[i][j+1][1], colorArr[i][j+1][2]));
                    g.fillRect(topLeft.getX()+j*width, topLeft.getY()+(i+1)*height, width, height);
                }
            }
        }
    }

    public int adjust(Point p){
        if(this.contains(p)){ //point must be in the button
            if(value<0){ //menu closed, open menu and return current value
                toggle();
                return value;
            }
            //else menu was open, find new value
            int val = (int)((p.getY()-topLeft.getY())/((bottomRight.getY()-topLeft.getY())/(itemCount+1))); //find value
            if(val == 0){ //clicked button header
                toggle();
                return value;
            }else{
                value = val; //set value and return
                return value;
            }
        }
        close(); //point not in button, close menu
        return value; 
    }

}
