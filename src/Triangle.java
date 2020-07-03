import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;

class TriangleComparator implements Comparator<Triangle> {
    @Override
    public int compare(Triangle t1, Triangle t2) {
        double z1 = (t1.triangle[0].z + t1.triangle[1].z + t1.triangle[2].z) / 3;
        double z2 = (t2.triangle[0].z + t2.triangle[1].z + t2.triangle[2].z) / 3;
        return Double.compare(z2, z1);
    }
}

public class Triangle {
    public Vec3D[] triangle;

    public Color color;

    public Triangle() {
        this.triangle = new Vec3D[3];
    }

    public Triangle(Vec3D[] triangle) {
        this.triangle = triangle;
    }

    public Triangle(Vec3D v1, Vec3D v2, Vec3D v3) {
        this.triangle = new Vec3D[]{v1, v2, v3};
    }

    public ArrayList<Triangle> clipAgainstPlane(Vec3D plane_p, Vec3D plane_n) {
        plane_n = plane_n.normalise();
        Vec3D[] inside_points = new Vec3D[3];
        int insidePointsCount = 0;
        Vec3D[] outside_points = new Vec3D[3];
        int outsidePointsCount = 0;

        double d0 = dist(triangle[0], plane_n, plane_p);
        double d1 = dist(triangle[1], plane_n, plane_p);
        double d2 = dist(triangle[2], plane_n, plane_p);

        if (d0 >= 0) {
            inside_points[insidePointsCount++] = triangle[0];
        } else {
            outside_points[outsidePointsCount++] = triangle[0];
        }

        if (d1 >= 0) {
            inside_points[insidePointsCount++] = triangle[1];
        } else {
            outside_points[outsidePointsCount++] = triangle[1];
        }

        if (d2 >= 0) {
            inside_points[insidePointsCount++] = triangle[2];
        } else {
            outside_points[outsidePointsCount++] = triangle[2];
        }

        ArrayList<Triangle> outList = new ArrayList<>();

        if (insidePointsCount == 0) return outList;
        if (insidePointsCount == 3) {
            Triangle out = new Triangle(this.triangle);
            out.color = this.color;
            outList.add(out);
            return outList;
        }
        if (insidePointsCount == 1) {
            Triangle t = new Triangle();
            t.triangle[0] = inside_points[0];
            t.triangle[1] = Vec3D.intersectPlane(plane_p, plane_n, inside_points[0], outside_points[0]);
            t.triangle[2] = Vec3D.intersectPlane(plane_p, plane_n, inside_points[0], outside_points[1]);
            t.color = this.color;
            outList.add(t);
        }

        if (insidePointsCount == 2) {
            Triangle t = new Triangle();
            Triangle t2 = new Triangle();

            t.triangle[0] = inside_points[0];
            t.triangle[1] = inside_points[1];
            t.triangle[2] = Vec3D.intersectPlane(plane_p, plane_n, inside_points[0], outside_points[0]);
            t.color = this.color;

            t2.triangle[0] = inside_points[1];
            t2.triangle[1] = t.triangle[2];
            t2.triangle[2] = Vec3D.intersectPlane(plane_p, plane_n, inside_points[1], outside_points[0]);
            t2.color = this.color;
            outList.add(t);
            outList.add(t2);

        }

        return outList;
    }

    private static double dist(Vec3D p, Vec3D plane_n, Vec3D plane_p) {
        p = p.normalise();
        return (plane_n.x * p.x + plane_n.y * p.y + plane_n.z * p.z - Vec3D.dotProduct(plane_n, plane_p));
    }


}