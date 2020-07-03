public class Vec3D {
    public double x;
    public double y;
    public double z;
    public double w = 1;

    public Vec3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vec3D(double x, double y, double z, double w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public Vec3D add(Vec3D v) {
        return new Vec3D(x + v.x, y + v.y, z + v.z);
    }

    public Vec3D sub(Vec3D v) {
        return new Vec3D(x - v.x, y - v.y, z - v.z);
    }

    public Vec3D multiply(double k) {
        return new Vec3D(x * k, y * k, z * k);
    }

    public Vec3D divide(double k) {
        return new Vec3D(x / k, y / k, z / k);
    }

    public double length() {
        return Math.sqrt(dotProduct(this, this));
    }

    public Vec3D normalise() {
        double l = length();
        return new Vec3D(x / l, y / l, z / l);
    }

    public static Vec3D crossProduct(Vec3D v1, Vec3D v2) {
        double x = v1.y * v2.z - v1.z * v2.y;
        double y = v1.z * v2.x - v1.x * v2.z;
        double z = v1.x * v2.y - v1.y * v2.x;
        return new Vec3D(x, y, z);
    }

    public static double dotProduct(Vec3D v1, Vec3D v2) {
        return v1.x * v2.x + v1.y * v2.y + v1.z * v2.z;
    }

    public static Vec3D intersectPlane(Vec3D plane_p, Vec3D plane_n, Vec3D lineStart, Vec3D lineEnd) {
        plane_n = plane_n.normalise();
        double plane_d = -dotProduct(plane_n, plane_p);
        double ad = dotProduct(lineStart, plane_n);
        double bd = dotProduct(lineEnd, plane_n);
        double t = (-plane_d - ad) / (bd - ad);
        Vec3D lineStartToEnd = lineEnd.sub(lineStart);
        Vec3D lineToIntersect  = lineStartToEnd.multiply(t);
        return lineStart.add(lineToIntersect);
    }
}