import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;
import java.awt.event.*;
import java.awt.geom.Path2D;
import java.io.File;
import java.util.Scanner;
import java.awt.*;

//TODO 
//color options
//menu
//hover over for stats in slice view

public class GraphPanel extends JPanel implements MouseListener, MouseMotionListener, KeyListener {
    static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    static final long serialVersionUID = 1;
    static final int WIDTH = screenSize.width;
    static final int HEIGHT = screenSize.height;
    static String state = "menu";
    static int mouseClickedX, mouseClickedY, mouseX, mouseY = 2000;
    static VerticalSlider slice = new VerticalSlider(new Point(0, 0), new Point(20, 200), 0, 100, 0);
    static CheckBox toSlice = new CheckBox(new Point(30, 10), new Point(40, 20));
    static CheckBox menuButton = new CheckBox(new Point(WIDTH/2-30, HEIGHT/2+30), new Point(WIDTH/2+30, HEIGHT/2+60));
    static DropDown colorButton = new DropDown(new Point(WIDTH-50, 10), new Point(WIDTH-10,30), 2);
    static final int CENTER_SIZE = 8;
    static Dragger centerPoint = new Dragger(new Point(250, 400), CENTER_SIZE);
    static double aAngle = 45;
    static double aAngleStart;
    static double bAngle = 20;
    static double bAngleStart;
    static Vector xVector = makeXVector(aAngle, bAngle);
    static Vector yVector = makeYVector(aAngle, bAngle);
    static final Point CENTER = new Point(250, 400);
    static int[][][] stats = new int[30][60][100]; // assists, rebound, points
    static String playerName = "";
    static int[][][] colorArr = new int[10][10][3];
    static int colorNumber = 0;

    static Cube c = new Cube(new Point3D(), 10);

    public GraphPanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        initColorArr(colorArr);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawString("state: " + state, 20, 100);
        if(!state.equals("menu")){
            slice.draw(g);
            toSlice.draw(g);
            colorButton.drawColor(g, colorArr);
            drawAxis(g, centerPoint.topLeft);
            centerPoint.draw(g);


            // g.drawString("X: " + xVector.toString(), 100, 20);
            // g.drawString("Y: " + yVector.toString(), 100, 50);
            g.drawString("" + slice.value.intValue(), 20, 210);
            g.drawString(playerName, 300, 20);
            int astOrder = Math.min((int)(Math.cos(bAngle*3.14159/180.0)+1),1); // one in the front 4 octants, zero elsewhere            
            int ptsOrder = (int)(Math.sin(bAngle*3.14159/180.0)+1); // zero in the left 4 octants, 1 elsewhere            
            for (int ast = stats.length-astOrder*stats.length-1+astOrder; (ast < stats.length && ast>=0); ast+=(2*astOrder-1)) {
                for (int tbr = 0; tbr < stats[0].length; tbr++) {
                    for (int pts = stats[0][0].length-ptsOrder*stats[0][0].length-1+ptsOrder; (pts < stats[0][0].length && pts>=0); pts+=(2*ptsOrder-1)) {
                        if (toSlice.value) {
                            if (pts == slice.value.intValue()) {
                                drawCube(g, new Cube(new Point3D(ast, pts, tbr), stats[ast][tbr][pts]), centerPoint.topLeft);
                            }
                        } else {
                            drawCube(g, new Cube(new Point3D(ast, pts, tbr), stats[ast][tbr][pts]), centerPoint.topLeft);
                        }
                    }
                }
            }
        }else{
            g.drawString("menu", WIDTH/2, HEIGHT/2);
            menuButton.drawPlain(g);
            g.setColor(Color.WHITE);
            g.fillRect(WIDTH/2-100, HEIGHT/2-15, 200, 30);
            g.setColor((Color.RED));
            g.setFont(new Font("Arial", Font.BOLD, 15));
            g.drawString(playerName, WIDTH/2-100, HEIGHT/2+10);
        }
    }

    public static void initArray(int[][][] arr, String name) {
        File f = new File("processed/" + InitStats.nameToID(name));
        if (!f.exists()) {
            String[] args = { name };
            InitStats.main(args);
        }
        try {
            Scanner s = new Scanner(f);
            while (s.hasNextInt()) {
                int pts = s.nextInt();
                int trb = s.nextInt();
                int ast = s.nextInt();
                arr[ast][trb][pts]++;
            }
            s.close();
        } catch (Exception e) {
            System.out.println("Player data for " + name + " not created");
        }
    }

    public static void initColorArr(int[][][] arr){
        //[color set number][frequency value][r/g/b]
        arr[0][1][0] = 0;
        arr[0][1][1] = 22;
        arr[0][1][2] = 216;

        arr[0][2][0] = 0;
        arr[0][2][1] = 100;
        arr[0][2][2] = 182;

        arr[0][3][0] = 0;
        arr[0][3][1] = 200;
        arr[0][3][2] = 100;

        arr[0][4][0] = 183;
        arr[0][4][1] = 250;
        arr[0][4][2] = 0;

        for (int i = 5; i < arr[0].length; i++) {
            arr[0][i][0] = 255;
            arr[0][i][1] = 255;
            arr[0][i][2] = 0;
        }
        

        arr[1][1][0] = 102;
        arr[1][1][1] = 18;
        arr[1][1][2] = 0;

        arr[1][2][0] = 186;
        arr[1][2][1] = 49;
        arr[1][2][2] = 5;

        arr[1][3][0] = 230;
        arr[1][3][1] = 119;
        arr[1][3][2] = 7;

        arr[1][4][0] = 250;
        arr[1][4][1] = 163;
        arr[1][4][2] = 6;

        for (int i = 5; i < arr[1].length; i++) {
            arr[1][i][0] = 255;
            arr[1][i][1] = 220;
            arr[1][i][2] = 0;
        }

    }

    public static void drawCube(Graphics g, Cube c, Point center) {
        if (c.colorValue == 0) {
            return;
        }
        int cv = c.colorValue;
        double red = colorArr[colorNumber][cv][0];
        double green = colorArr[colorNumber][cv][1];
        double blue = colorArr[colorNumber][cv][2];
        
        int X = c.corner.X * c.scale;
        int Y = c.corner.Y * c.scale;
        int Z = c.corner.Z * c.scale;
        int[] xArr = new int[8];
        int[] yArr = new int[8];

        for (int i = 0; i < yArr.length; i++) {
            xArr[i] = (int) (xVector
                    .dotProduct(new Vector(X + c.scale * (i / 4), Y + c.scale * ((i / 2) % 2), Z + c.scale * (i % 2)))
                    + center.X + 0.5);
            yArr[i] = (int) (yVector
                    .dotProduct(new Vector(X + c.scale * (i / 4), Y + c.scale * ((i / 2) % 2), Z + c.scale * (i % 2)))
                    + center.Y + 0.5);
        }

        Graphics2D g2d = (Graphics2D) g;

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

    public static void drawAxis(Graphics g, Point center) {
        int xLength = 300;
        int yLength = 900;
        int zLength = 300;

        int x0 = (int) (xVector.dotProduct(new Vector(0, 0, 0))) + center.X;
        int y0 = (int) (yVector.dotProduct(new Vector(0, 0, 0))) + center.Y;

        int x1 = (int) (xVector.dotProduct(new Vector(0 + xLength, 0, 0))) + center.X;
        int y1 = (int) (yVector.dotProduct(new Vector(0 + xLength, 0, 0))) + center.Y;

        int x2 = (int) (xVector.dotProduct(new Vector(0, 0 + yLength, 0))) + center.X;
        int y2 = (int) (yVector.dotProduct(new Vector(0, 0 + yLength, 0))) + center.Y;

        int x3 = (int) (xVector.dotProduct(new Vector(0, 0, 0 + zLength))) + center.X;
        int y3 = (int) (yVector.dotProduct(new Vector(0, 0, 0 + zLength))) + center.Y;

        g.setColor(Color.WHITE);
        g.drawLine(x0, y0, x1, y1); // x
        g.drawLine(x0, y0, x2, y2); // y
        g.drawLine(x0, y0, x3, y3); // z
        g.drawString("Assists", x1, y1); // x
        g.drawString("Points", x2, y2); // y
        g.drawString("Rebounds", x3, y3); // z
    }

    public static Vector makeXVector(double a, double b) {
        a /= 180;
        b /= 180;
        a *= 3.14159;
        b *= 3.14159;
        return new Vector(-Math.sin(b), Math.cos(b), 0);
    }

    public static Vector makeYVector(double a, double b) {
        a /= 180;
        b /= 180;
        a *= 3.14159;
        b *= 3.14159;
        return new Vector(Math.sin(a) * Math.cos(b), Math.sin(a) * Math.sin(b), -Math.cos(a));
    }

    public static double boundAngle(double x) {
        x = Math.max(0, x);
        x = Math.min(x, 90);
        return x;
    }
    
    public static double boundBAngle(double x) {
        if(x<0)
            x+=360;
        if(x>=360)
            x-=360;
        // x = Math.max(0, x);
        // x = Math.min(x, 180);
        return x;
    }

    public static int boundColor(double x) {
        x += 0.5;
        x = Math.max(0, x);
        x = Math.min(x, 255);
        return (int) x;
    }

    public void mouseClicked(MouseEvent e) {
        if (toSlice.contains(new Point(mouseClickedX, mouseClickedY))) {
            toSlice.toggle();
            repaint();
        }
        
    }

    @Override
    public void mousePressed(MouseEvent e) {
        mouseClickedX = e.getX() - 5;
        mouseClickedY = e.getY() - 30;
        mouseX = e.getX() - 5;
        mouseY = e.getY() - 30;
        aAngleStart = aAngle;
        bAngleStart = bAngle;
        if(state.equals("menu")){
            if(menuButton.contains(new Point(mouseClickedX, mouseClickedY))){
                menuButton.toggle();
                initArray(stats, playerName);
                state = "run";
                repaint();
            }
            return;
        }
        state = "adjust";
        colorButton.adjust(new Point(mouseClickedX, mouseClickedY));
        colorNumber = Math.abs(colorButton.value)-1;
        if (slice.contains(new Point(mouseClickedX, mouseClickedY)) && toSlice.value) {
            slice.setValue(new Point(mouseX, mouseY));
        } else if(centerPoint.contains(new Point(mouseClickedX, mouseClickedY))){
            centerPoint.value = CENTER_SIZE*2;
        }else {
            state = "run";
        }
        repaint();
    }

    
    @Override
    public void mouseDragged(MouseEvent e) {
        mouseX = e.getX() - 5;
        mouseY = e.getY() - 30;
        if(state.equals("menu")){
            return;
        }else if (state.equals("adjust")) {
            if (slice.contains(new Point(mouseX, mouseY))) {
            // if (mouseY > slice.topLeft.Y && mouseY < slice.bottomRight.Y) {
                slice.setValue(new Point(mouseX, mouseY));
            }else if(centerPoint.value == CENTER_SIZE*2){
                centerPoint.topLeft = new Point(mouseX, mouseY);
                centerPoint.bottomRight = new Point(mouseX+CENTER_SIZE, mouseY+CENTER_SIZE);
            }
            repaint();
        } else if (state.equals("run")) {
            aAngle = boundAngle(aAngleStart + (mouseY - mouseClickedY) / 10);
            bAngle = boundBAngle(bAngleStart + (mouseClickedX - mouseX) / 10);
            xVector = makeXVector(aAngle, bAngle);
            yVector = makeYVector(aAngle, bAngle);
            repaint();
        }
        
    }
    
    @Override
    public void mouseReleased(MouseEvent e) {
        // if(state.equals("adjust")){
        // repaint();
        // }
        centerPoint.value = CENTER_SIZE;
        repaint();
        if(!state.equals("menu"))
            state = "run";
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
        if(c == 8){
            playerName = playerName.substring(0, Math.max(playerName.length()-1, 0));
        }else{
            playerName+=c;
        }
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {  
        if(e.getKeyCode() == KeyEvent.VK_ENTER && state.equals("menu")){
            String temp = playerName;
            playerName = "Working..";
            repaint();
            System.out.println("Working...");
            repaint();
            initArray(stats, temp);
            state = "run";
            playerName = temp;
            repaint();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    public static void pause(int delay) {
        long start = System.currentTimeMillis();
        while(start >= System.currentTimeMillis() - delay); // do nothing
    }
}
