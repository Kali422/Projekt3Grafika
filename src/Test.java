import java.util.ArrayList;

public class Test {

    public static void main(String[] args) {
        ArrayList<Triangle> list = new ArrayList<>();
        list.add(new Triangle(new Vec3D(1,1,10), new Vec3D(2,2,15), new Vec3D(10,10,10)));
        list.add(new Triangle(new Vec3D(1,10,5), new Vec3D(1,15,2), new Vec3D(9,8,3)));
        list.sort(new TriangleComparator());
        list.toString();


    }
}
