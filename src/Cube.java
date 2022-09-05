
public class Cube {

    int scale = 15;
    // static Vector xVector = new Vector(0.3535, 0.866, 0.3535);
    // static Vector yVector = new Vector(0.6123, -0.5, 0.6123);
    // static Vector xVector = new Vector(0.707, -0.707, 0);
    // static Vector yVector = new Vector(0.3535, 0.3535, -0.866);

    protected int colorValue;
    protected Point3D corner;

    public Cube(){
        colorValue = 0;
        corner = new Point3D();
    }

    public Cube( Point3D p, int cv){
        colorValue = cv;
        corner = p;
    }

    public Cube(Point3D p){
        colorValue = 0;
        corner = p;
    }

    public double getX(){
        return corner.X;
    }

    public double getY(){
        return corner.Y;
    }

    public double getZ(){
        return corner.Z;
    }

    public Point3D getPoint(){
        return corner;
    }

    public int getGolorValue(){
        return colorValue;
    }


    public void setPosition(Point3D p){
        corner = p;
    }

    public void setColorValue(int cv){
        colorValue = cv;
    }

    
}
