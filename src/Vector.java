public class Vector {

    double X, Y, Z;

    public Vector() {
        X = Y = Z = 0;
    }

    public Vector(double x, double y, double z) {
        X = x;
        Y = y;
        Z = z;
    }

    public double dotProduct(Vector p) {
        return this.X * p.X + this.Y * p.Y + this.Z * p.Z;
    }

    public double dotProduct(Point3D p) {
        return this.X * p.X + this.Y * p.Y + this.Z * p.Z;
    }

    public String toString(){
        return "[" + X + ", " + Y + ", " + Z + "]";
    }

}
