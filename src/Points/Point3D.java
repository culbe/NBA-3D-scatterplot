package Points;
public class Point3D extends Point{

    int Z;
    
    public Point3D(){
        super();
    }

    public Point3D(int x, int y, int z){
        X = x;
        Y = y;
        Z = z;
    }

    public void setZ(int z){
        Z = z;
    }

    public int getZ(){
        return Z;
    }

}
