import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;

import Buttons.*;
import Points.Point;
import Points.Point3D;

import java.awt.event.*;
import java.awt.geom.Path2D;
import java.io.File;
import java.util.Scanner;
import java.awt.*;

/*TODO 
-clean up menu
-fix stat creation from input
-hover over for stats in slice view
*/

public class GraphPanel extends JPanel implements MouseListener, MouseMotionListener, KeyListener {
    static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    static final long serialVersionUID = 1;
    static final int WIDTH = screenSize.width;
    static final int HEIGHT = screenSize.height;
    static String state = "menu";
    static int mouseClickedX, mouseClickedY, mouseX, mouseY = 2000; //offscreen
    
    //buttons and boxes
    static final Point SLICE_POINT = new Point(WIDTH/2-200, 10);
    static HorizontalSlider slice = new HorizontalSlider(incrimentPoint(SLICE_POINT, 0, 20), incrimentPoint(SLICE_POINT, 200, 40), 0, 100, 0);
    static CheckBox toSlice = new CheckBox(incrimentPoint(SLICE_POINT, 190, 0), incrimentPoint(SLICE_POINT, 200, 10));
    
    static final Point START_POINT = new Point(WIDTH/2-30, HEIGHT/2+30);
    static final Point MENU_POINT = new Point(WIDTH-70, 10);
    static SimpleButton menuButton = new SimpleButton(START_POINT, incrimentPoint(START_POINT, 60, 30), "Start");
    
    static final Point COLOR_POINT = new Point(WIDTH-270, 10);
    static DropDown colorButton = new DropDown(COLOR_POINT, incrimentPoint(COLOR_POINT, 100, 20), 3);
    
    static final Point BOXES_START = new Point(WIDTH/2+150, 20);
    static CheckBox onePoint = new CheckBox(BOXES_START, incrimentPoint(BOXES_START, 10, 10), true);
    static CheckBox twoPoint = new CheckBox(incrimentPoint(BOXES_START, 20, 0), incrimentPoint(BOXES_START, 30, 10), true);
    static CheckBox threePoint = new CheckBox(incrimentPoint(BOXES_START, 40, 0), incrimentPoint(BOXES_START, 50, 10), true);
    static CheckBox fourPoint = new CheckBox(incrimentPoint(BOXES_START, 60, 0), incrimentPoint(BOXES_START, 70, 10), true);
    static CheckBox fivePoint = new CheckBox(incrimentPoint(BOXES_START, 80, 0), incrimentPoint(BOXES_START, 90, 10), true);
    static CheckBox[] boxes = {onePoint, twoPoint, threePoint, fourPoint, fivePoint};
    
    static final int OG_CENTERPOINT_SIZE = 8;
    static final Point CENTER = new Point(250, 400); //origin
    static Dragger centerPoint = new Dragger(CENTER, OG_CENTERPOINT_SIZE);

    //display variables
    static double aAngle = 45;
    static double aAngleStart;
    static double bAngle = 20;
    static double bAngleStart;

    static Vector xVector = makeXVector(aAngle, bAngle);
    static Vector yVector = makeYVector(aAngle, bAngle);
    static int[][][] stats = new int[30][60][100]; // assists, rebound, points
    static String playerName = "Lebron James"; //player to display
    static int[][][] colorArr = new int[10][6][3]; //array containing color scheme information
    static int colorNumber = 0; //number for color scheme option

    public GraphPanel() { //initalize panel
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.LIGHT_GRAY);
        initColorArr(colorArr);
    }

    public void paintComponent(Graphics g) { //method that draws everything
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(3));
        g.setFont(new Font("Rockwell", Font.BOLD, 15));
        // g.drawString("state: " + state, 20, 100);
        if(!state.equals("menu")){
            //draw axis
            drawAxis(g, centerPoint.getTopLeft());
            centerPoint.draw(g);
            //draw cubes
            int astOrder = Math.min((int)(Math.cos(bAngle*3.14159/180.0)+1),1); // one in the front 4 octants, zero elsewhere            
            int ptsOrder = (int)(Math.sin(bAngle*3.14159/180.0)+1); // zero in the left 4 octants, 1 elsewhere            
            for (int ast = stats.length-astOrder*stats.length-1+astOrder; (ast < stats.length && ast>=0); ast+=(2*astOrder-1)) {
                for (int tbr = 0; tbr < stats[0].length; tbr++) {
                    for (int pts = stats[0][0].length-ptsOrder*stats[0][0].length-1+ptsOrder; (pts < stats[0][0].length && pts>=0); pts+=(2*ptsOrder-1)) {
                        if(!toSlice.getValue() || pts == slice.getValue().intValue())
                            drawCube(g, new Cube(new Point3D(ast, pts, tbr), stats[ast][tbr][pts]), centerPoint.getTopLeft());
                    }
                }
            }

            if(astOrder==0){
                //z axis
                g.setColor(Color.WHITE);
                int x3 = (int) (xVector.dotProduct(new Vector(0, 0, 0 + 300))) + centerPoint.getTopLeft().getX();
                int y3 = (int) (yVector.dotProduct(new Vector(0, 0, 0 + 300))) + centerPoint.getTopLeft().getY();
                g.drawLine(centerPoint.getTopLeft().getX(), centerPoint.getTopLeft().getY(), x3, y3);
                g.drawString("Rebounds", x3, y3);
            }

            g.setColor(new Color(80,80,80));
            g.fillRect(0, 0, WIDTH, 60);

            slice.draw(g);
            toSlice.draw(g);
            for (CheckBox checkBox : boxes) {
                checkBox.draw(g);
            }
            colorButton.drawColor(g, colorArr);
            
            g.setColor(Color.BLACK);
            g.drawString("Drag to rotate graph", 10, 20);
            g.drawString("Shift to plan around", 10, 40);
            g.drawString("Displaying Player:", 200, 20);
            g.drawString("" + playerName, 200, 40);
            g.drawString("Filter by points", SLICE_POINT.getX(), SLICE_POINT.getY()+10);
            g.drawString("Frequency", BOXES_START.getX(), BOXES_START.getY()-8);
            g.drawString("1   2   3   4   5+", BOXES_START.getX(), BOXES_START.getY()+30);
            g.drawString("Select Color", COLOR_POINT.getX(), COLOR_POINT.getY()+18);
        }else{ //State is menu
            g.setFont(new Font("Rockwell", Font.BOLD, 40));
            g.setColor(Color.BLACK);
            g.drawString("Welcome to NBA Career grapher!", WIDTH/2-300, 300);
            g.setFont(new Font("Rockwell", Font.BOLD, 15));
            g.drawString("menu", WIDTH/2, HEIGHT/2);
            g.setColor(Color.WHITE);
            g.fillRect(WIDTH/2-100, HEIGHT/2-15, 200, 30);
            g.setColor((Color.RED));
            g.setFont(new Font("Rockwell", Font.BOLD, 15));
            g.drawString(playerName, WIDTH/2-100, HEIGHT/2+10);
        }
        //Always draw menu button
        menuButton.draw(g);
    }

    /***
     * Takes the text file of stats and turns it into the array
     * @param name Full name of the player
     * @return the 3D array of stats
     */
    public int[][][] initArray(String name) {
        File f = new File("processed/" + InitStats.nameToID(name));
        if (!f.exists()) { //No existing file, make one
            String[] args = { name };
            //TODO make loading screen
            InitStats.main(args);
        }
        int[][][] arr = new int[30][60][100]; // assists, rebound, points
        try {
            Scanner s = new Scanner(f);
            while (s.hasNextInt()) { //iterate over stats and increase array 
                int pts = s.nextInt();
                int trb = s.nextInt();
                int ast = s.nextInt();
                if(arr[ast][trb][pts]<5)
                    arr[ast][trb][pts]++;
            }
            s.close();
        } catch (Exception e) {
            System.out.println("Player data for " + name + " not created");
        }
        return arr;
    }
    
    /**
     * Sets the color schemes with hardcoded values
     * @param arr the array to intialize
     */
    public static void initColorArr(int[][][] arr){
        //[color scheme number][frequency value][r/g/b]
        
        //Color scheme 1:
        //medium blue
        arr[0][1][0] = 87;
        arr[0][1][1] = 130;
        arr[0][1][2] = 255;
        //teal
        arr[0][2][0] = 0;
        arr[0][2][1] = 129;
        arr[0][2][2] = 125;
        //green
        arr[0][3][0] = 0;
        arr[0][3][1] = 152;
        arr[0][3][2] = 62;
        //yellow
        arr[0][4][0] = 230;
        arr[0][4][1] = 230;
        arr[0][4][2] = 0;
        //orange
        arr[0][5][0] = 242;
        arr[0][5][1] = 145;
        arr[0][5][2] = 0;
        
        //Color scheme 2:
        //Dark red
        arr[1][1][0] = 102;
        arr[1][1][1] = 18;
        arr[1][1][2] = 0;
        //medium red
        arr[1][2][0] = 186;
        arr[1][2][1] = 49;
        arr[1][2][2] = 5;
        //dark orange
        arr[1][3][0] = 230;
        arr[1][3][1] = 119;
        arr[1][3][2] = 7;
        //orange
        arr[1][4][0] = 250;
        arr[1][4][1] = 163;
        arr[1][4][2] = 6;
        //dark yellow
        arr[1][5][0] = 255;
        arr[1][5][1] = 220;
        arr[1][5][2] = 0;

        //Color scheme 3:
        //lavender
        arr[2][1][0] = 184;
        arr[2][1][1] = 137;
        arr[2][1][2] = 255;
        //dark lavender
        arr[2][2][0] = 184;
        arr[2][2][1] = 96;
        arr[2][2][2] = 255;
        //magenta
        arr[2][3][0] = 200;
        arr[2][3][1] = 0;
        arr[2][3][2] = 234;
        //red magenta
        arr[2][4][0] = 208;
        arr[2][4][1] = 0;
        arr[2][4][2] = 122;
        //dark red
        arr[2][5][0] = 161;
        arr[2][5][1] = 0;
        arr[2][5][2] = 40;
        
    }

    /***
     * Basis of the rendering, draws cubes from their position and the viewpoint angles
     * @param g must pass Graphics object
     * @param c The cube to draw
     * @param center The origin point 
     */
    public static void drawCube(Graphics g, Cube c, Point center) {
        //must check that certain game frequencies have not been filtered out
        if (c.colorValue == 0) {
            return;
        }
        if(c.colorValue==1 && !onePoint.getValue()){
            return;
        }
        if(c.colorValue==2 && !twoPoint.getValue()){
            return;
        }
        if(c.colorValue==3 && !threePoint.getValue()){
            return;
        }
        if(c.colorValue==4 && !fourPoint.getValue()){
            return;
        }
        if(c.colorValue>=5 && !fivePoint.getValue()){
            return;
        }

        //Get the color to draw the cube from the cube's cv and the color array
        int cv = c.colorValue;
        double red = colorArr[colorNumber][cv][0];
        double green = colorArr[colorNumber][cv][1];
        double blue = colorArr[colorNumber][cv][2];
        
        //Scale out the cube from its 3D corner
        int X = c.corner.getX() * c.scale;
        int Y = c.corner.getY() * c.scale;
        int Z = c.corner.getZ() * c.scale;
        int[] xArr = new int[8]; //array for the xPos of all corners of cube in 2D
        int[] yArr = new int[8]; //same for yPos

        for (int i = 0; i < yArr.length; i++) {
            //Takes dot product of angle vector and each 3D corner to get the 2D position
            //the division and mod are to get the offset for the corners
            //The position of the origin is added on last
            xArr[i] = (int) (xVector
                    .dotProduct(new Vector(X + c.scale * (i / 4), Y + c.scale * ((i / 2) % 2), Z + c.scale * (i % 2)))
                    + center.getX() + 0.5);
            yArr[i] = (int) (yVector
                    .dotProduct(new Vector(X + c.scale * (i / 4), Y + c.scale * ((i / 2) % 2), Z + c.scale * (i % 2)))
                    + center.getY() + 0.5);
        }

        Graphics2D g2d = (Graphics2D) g;

        //each face of teh cube is a paralellogram defined by a path araound the vertices

        Path2D.Double parallelogram0;
        parallelogram0 = new Path2D.Double(); //top
        parallelogram0.moveTo(xArr[7], yArr[7]);
        parallelogram0.lineTo(xArr[5], yArr[5]);
        parallelogram0.lineTo(xArr[1], yArr[1]);
        parallelogram0.lineTo(xArr[3], yArr[3]);
        parallelogram0.closePath();
        g2d.setColor(new Color(boundColor(red - 20), boundColor(green - 20), boundColor(blue - 20)));
        g2d.fill(parallelogram0);
        

        if(bAngle<180){
            Path2D.Double parallelogram2; //right
            parallelogram2 = new Path2D.Double();
            parallelogram2.moveTo(xArr[7], yArr[7]);
            parallelogram2.lineTo(xArr[3], yArr[3]);
            parallelogram2.lineTo(xArr[2], yArr[2]);
            parallelogram2.lineTo(xArr[6], yArr[6]);
            parallelogram2.closePath();
            g2d.setColor(new Color(boundColor(red), boundColor(green), boundColor(blue)));
            g2d.fill(parallelogram2);
        }        
        if(bAngle<90 || bAngle>270){
            Path2D.Double parallelogram1; //front
            parallelogram1 = new Path2D.Double();
            parallelogram1.moveTo(xArr[7], yArr[7]);
            parallelogram1.lineTo(xArr[5], yArr[5]);
            parallelogram1.lineTo(xArr[4], yArr[4]);
            parallelogram1.lineTo(xArr[6], yArr[6]);
            parallelogram1.closePath();
            g2d.setColor(new Color(boundColor(red + 10), boundColor(green + 10), boundColor(blue + 10)));
            g2d.fill(parallelogram1);
        }
        if(bAngle>90 && bAngle<270){
            Path2D.Double parallelogram3; //back
            parallelogram3 = new Path2D.Double();
            parallelogram3.moveTo(xArr[0], yArr[0]);
            parallelogram3.lineTo(xArr[2], yArr[2]);
            parallelogram3.lineTo(xArr[3], yArr[3]);
            parallelogram3.lineTo(xArr[1], yArr[1]);
            parallelogram3.closePath();
            g2d.setColor(new Color(boundColor(red + 10), boundColor(green + 10), boundColor(blue + 10)));
            g2d.fill(parallelogram3);
        }
        if(bAngle>180){
            Path2D.Double parallelogram4; //left
            parallelogram4 = new Path2D.Double();
            parallelogram4.moveTo(xArr[0], yArr[0]);
            parallelogram4.lineTo(xArr[1], yArr[1]);
            parallelogram4.lineTo(xArr[5], yArr[5]);
            parallelogram4.lineTo(xArr[4], yArr[4]);
            parallelogram4.closePath();
            g2d.setColor(new Color(boundColor(red), boundColor(green), boundColor(blue)));
            g2d.fill(parallelogram4);
        }
    }

    /***
     * Draws the axis for the grapg
     * @param g must pass the Graphics object
     * @param center the orgin of the graph
     */
    public static void drawAxis(Graphics g, Point center) {
        int xLength = 300;
        int yLength = 900;
        int zLength = 300;

        g.setColor(Color.WHITE);

        //origin
        int x0 = center.getX();
        int y0 = center.getY();
        
        //x axis
        int x1 = (int) (xVector.dotProduct(new Vector(0 + xLength, 0, 0))) + center.getX();
        int y1 = (int) (yVector.dotProduct(new Vector(0 + xLength, 0, 0))) + center.getY();
        g.drawLine(x0, y0, x1, y1);
        g.drawString("Assists", x1, y1);
        
        //y axis
        int x2 = (int) (xVector.dotProduct(new Vector(0, 0 + yLength, 0))) + center.getX();
        int y2 = (int) (yVector.dotProduct(new Vector(0, 0 + yLength, 0))) + center.getY();
        g.drawLine(x0, y0, x2, y2);
        g.drawString("Points", x2, y2);

        //z axis
        int x3 = (int) (xVector.dotProduct(new Vector(0, 0, 0 + zLength))) + center.getX();
        int y3 = (int) (yVector.dotProduct(new Vector(0, 0, 0 + zLength))) + center.getY();
        g.drawLine(x0, y0, x3, y3);
        g.drawString("Rebounds", x3, y3);
    }

    /***
     * Creates a vector for calculating the xPos from the angles in degrees
     * @param a
     * @param b
     * @return
     */
    public static Vector makeXVector(double a, double b) {
        //must convert to radians
        a /= 180;
        b /= 180;
        a *= 3.14159;
        b *= 3.14159;
        return new Vector(-Math.sin(b), Math.cos(b), 0);
    }

    /***
     * Creates a vector for calculating the yPos from the angles in degrees
     * @param a
     * @param b
     * @return
     */
    public static Vector makeYVector(double a, double b) {
        //must convert to radians
        a /= 180;
        b /= 180;
        a *= 3.14159;
        b *= 3.14159;
        return new Vector(Math.sin(a) * Math.cos(b), Math.sin(a) * Math.sin(b), -Math.cos(a));
    }

    /***
     * Keeps angle in first quadrant
     * @param x Angle
     * @return new angle
     */
    public static double boundAngle(double x) { 
        x = Math.max(0, x);
        x = Math.min(x, 90);
        return x;
    }
    
    /***
     * Keeps angle between 0 and 360
     * @param x Angle
     * @return new angle
     */
    public static double boundBAngle(double x) { 
        if(x<0)
            x+=360;
        if(x>=360)
            x-=360;
        // x = Math.max(0, x);
        // x = Math.min(x, 180);
        return x;
    }

    /***
     * Keeps color rgb inputs between 0 and 255
     * @param x any r/g/b value
     * @return safe value 
     */
    public static int boundColor(double x) {
        x += 0.5;
        x = Math.max(0, x);
        x = Math.min(x, 255);
        return (int) x;
    }

    public void mouseClicked(MouseEvent e) {
        if(!state.equals("menu")){ //not menu
            if (toSlice.contains(new Point(mouseClickedX, mouseClickedY))) {
                toSlice.toggle();
                repaint();
            }
            for (CheckBox checkBox : boxes) {
                if (checkBox.contains(new Point(mouseClickedX, mouseClickedY))) {
                    checkBox.toggle();
                    repaint();
                }
            }
        }
    }
        
    @Override
    public void mousePressed(MouseEvent e) {
        //update mouse positions
        mouseClickedX = e.getX() - 5;
        mouseClickedY = e.getY() - 30;
        mouseX = e.getX() - 5;
        mouseY = e.getY() - 30;
        aAngleStart = aAngle;
        bAngleStart = bAngle;
        if(menuButton.contains(new Point(mouseClickedX, mouseClickedY))){
            toggleMenuButton();
            return;
        }
        if(!state.equals("menu")){ //not menu
            colorButton.adjust(new Point(mouseClickedX, mouseClickedY));
            colorNumber = Math.abs(colorButton.getValue())-1;
            if (slice.contains(new Point(mouseClickedX, mouseClickedY)) && toSlice.getValue()) {
                state = "adjust slice";
                slice.setValue(new Point(mouseX, mouseY));
            } else if(centerPoint.contains(new Point(mouseClickedX, mouseClickedY)) || centerPoint.getValue() == OG_CENTERPOINT_SIZE*2){
                state = "adjust center";
                centerPoint.setValue(OG_CENTERPOINT_SIZE*2);
            }else {
                state = "run";
            }
        }
        repaint();
    }

    
    @Override
    public void mouseDragged(MouseEvent e) {
        int oldX = mouseX;
        int oldY = mouseY;
        mouseX = e.getX() - 5;
        mouseY = e.getY() - 30;
        if(state.equals("menu")){
            return;
        }else if (state.equals("adjust slice")) {
            slice.setValue(new Point(mouseX, mouseY));
        }else if (state.equals("adjust center")) {
            centerPoint.setTopLeft(incrimentPoint(centerPoint.getTopLeft(), mouseX-oldX, mouseY-oldY));
            centerPoint.setBottomRight(incrimentPoint(centerPoint.getTopLeft(), OG_CENTERPOINT_SIZE, OG_CENTERPOINT_SIZE));
        } else if (state.equals("run")) {
            aAngle = boundAngle(aAngleStart + (mouseY - mouseClickedY) / 10);
            bAngle = boundBAngle(bAngleStart + (mouseClickedX - mouseX) / 10);
            xVector = makeXVector(aAngle, bAngle);
            yVector = makeYVector(aAngle, bAngle);
        }
        repaint();
        
    }
    
    @Override
    public void mouseReleased(MouseEvent e) {
        if(!state.equals("menu")){
            if(centerPoint.contains(new Point(mouseX-5, mouseY-5))){
                centerPoint.setValue(OG_CENTERPOINT_SIZE);
            }
            if(centerPoint.getValue()==OG_CENTERPOINT_SIZE){
                state = "run";
            }
        }
        repaint();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
        char c = e.getKeyChar();
        if(c == 8){ //backspace
            playerName = playerName.substring(0, Math.max(playerName.length()-1, 0));
        }else{ //type
            playerName+=c;
        }
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {  
        if(e.getKeyCode() == KeyEvent.VK_ENTER && state.equals("menu")){
            //enter from menu starts running
            toggleMenuButton();
        }else if(e.getKeyCode() == KeyEvent.VK_SHIFT && state.equals("run")){
            //start panning
            state = "adjust center";
            centerPoint.setValue(OG_CENTERPOINT_SIZE*2);
            repaint();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_SHIFT && state.equals("adjust center")){ 
            //Stop panning
            state = "run";
            centerPoint.setValue(OG_CENTERPOINT_SIZE);
            repaint();
        }
    }

    /***
     * A method used to simplify the shifting of points
     * @param p The point to shift
     * @param x The x offset
     * @param y The y offset
     * @return shifted Point
     */
    public static Point incrimentPoint(Point p, int x, int y){
        return new Point(p.getX()+x, p.getY()+y);
    }

    /***
     * Handles the behavior when the menu button is clicked.
     * Either goes to runscreen or menu.
     * Moves the position of the button and resets global vars as needed
     */
    public void toggleMenuButton(){
        if(state.equals("menu")){
            stats = initArray(playerName);
            //reset globals and buttons
            aAngle = 45;
            bAngle = 20;
            xVector = makeXVector(aAngle, bAngle);
            yVector = makeYVector(aAngle, bAngle);
            centerPoint.setBottomRight(CENTER);
            for (CheckBox checkBox : boxes) {
                checkBox.setValue(true);
            }
            toSlice.setValue(false);
            slice.setValue(0.0);
            colorButton.close();
            state = "run";
            //shift button to new position
            menuButton.setTopLeft(MENU_POINT);
            menuButton.setBottomRight(incrimentPoint(MENU_POINT, 60, 30));
            menuButton.setValue("Menu");
            repaint();
        }else if(state.equals("run")){
            state = "menu";
            //shift button to new position
            menuButton.setTopLeft(START_POINT);
            menuButton.setBottomRight(incrimentPoint(START_POINT, 60, 30));
            menuButton.setValue("Start");
            repaint();
        }
    }

}
