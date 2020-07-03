import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Queue;
import java.util.StringJoiner;
import javax.swing.*;


class Draw extends JPanel {

    public final BufferedImage canvas;
    private final int width;
    private final int height;
    double near, far, fov, aspect;
    Matrix4x4 matProj;
    Mesh m;
    Vec3D vLookDir, vCam, light;


    public Draw(Mesh mesh, int width, int height) {
        this.width = width;
        this.height = height;
        canvas = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        fillCanvas(Color.WHITE);
        near = 0.1;
        far = 1000;
        fov = 90;
        aspect = (double) height / width;
        m = mesh;
        matProj = Matrix4x4.makeProjection(fov, aspect, near, far);
        vLookDir = new Vec3D(0, 0, 1);
        vCam = new Vec3D(0, 2, 3);
        light = new Vec3D(0, 1, -2);

    }

    public Dimension getPreferredSize() {
        return new Dimension(canvas.getWidth(), canvas.getHeight());
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(canvas, null, null);
    }


    public void fillCanvas(Color c) {
        int color = c.getRGB();
        for (int x = 0; x < canvas.getWidth(); x++) {
            for (int y = 0; y < canvas.getHeight(); y++) {
                canvas.setRGB(x, y, color);
            }
        }
        repaint();
    }


    public void drawLine(Color c, int x1, int y1, int x2, int y2) {
        int kx = (x1 <= x2) ? 1 : -1;
        int ky = (y1 <= y2) ? 1 : -1;
        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);

        int curr_x = x1;
        int curr_y = y1;

        int color = c.getRGB();

        if (curr_x < width && curr_y < height) {
            canvas.setRGB(curr_x, curr_y, color);
        }

        double e;
        if (!(dx < dy)) {
            e = (double) dx / 2;
            for (int i = 0; i < dx; i++) {
                curr_x = curr_x + kx;
                e = e - dy;
                if (!(e >= 0)) {
                    curr_y = curr_y + ky;
                    e = e + dx;
                }
                if (curr_x < width && curr_y < height) {
                    canvas.setRGB(curr_x, curr_y, color);
                }
            }
        } else {
            e = (double) dy / 2;
            for (int i = 0; i < dy; i++) {
                curr_y = curr_y + ky;
                e = e - dx;
                if (!(e >= 0)) {
                    curr_x = curr_x + kx;
                    e = e + dy;
                }
                if (curr_x < width && curr_y < height) {
                    canvas.setRGB(curr_x, curr_y, color);
                }
            }

        }

    }

    public void drawLine(Color c, Vec3D v1, Vec3D v2) {
        drawLine(c, (int) v1.x, (int) v1.y, (int) v2.x, (int) v2.y);

    }

    public void drawHorizontLine(Color c, int x1, int x2, int y) {
        int color = c.getRGB();
        if (x1 > x2) {
            int tmp = x1;
            x1 = x2;
            x2 = tmp;
        }
        if (x2 >= width) {
            x2 = width - 1;
        }
        if (y >= height) {
            y = height - 1;
        }
        for (int i = x1; i <= x2; i++) {
            canvas.setRGB(i, y, color);
        }
    }

    public void drawTriangle(Color c, Triangle t) {
        drawLine(c, t.triangle[0], t.triangle[1]);
        drawLine(c, t.triangle[1], t.triangle[2]);
        drawLine(c, t.triangle[2], t.triangle[0]);

    }

    public void fillBottomFlatTriangle(Color c, double x1, double y1, double x2, double y2, double x3, double y3) {
        double invslope1 = (x2 - x1) / (y2 - y1);
        double invslope2 = (x3 - x1) / (y3 - y1);

        double curx1 = x1;
        double curx2 = x1;

        for (int scanlineY = (int) y1; scanlineY <= y2; scanlineY++) {
            drawHorizontLine(c, (int) curx1, (int) curx2, scanlineY);
            curx1 += invslope1;
            curx2 += invslope2;
        }
    }

    public void fillTopFlatTriangle(Color c, double x1, double y1, double x2, double y2, double x3, double y3) {
        double invslope1 = (x3 - x1) / (y3 - y1);
        double invslope2 = (x3 - x2) / (y3 - y2);

        double curx1 = x3;
        double curx2 = x3;

        for (int scanlineY = (int) y3; scanlineY > y1; scanlineY--) {
            drawHorizontLine(c, (int) curx1, (int) curx2, scanlineY);
            curx1 -= invslope1;
            curx2 -= invslope2;
        }
    }

    public void paintTriangle(Color c, Triangle t) {
        Vec3D tmp;
        if (t.triangle[0].y > t.triangle[1].y) {
            tmp = t.triangle[0];
            t.triangle[0] = t.triangle[1];
            t.triangle[1] = tmp;
        }
        if (t.triangle[0].y > t.triangle[2].y) {
            tmp = t.triangle[0];
            t.triangle[0] = t.triangle[2];
            t.triangle[2] = tmp;
        }
        if (t.triangle[1].y > t.triangle[2].y) {
            tmp = t.triangle[1];
            t.triangle[1] = t.triangle[2];
            t.triangle[2] = tmp;
        }

        int x1 = (int) t.triangle[0].x;
        int x2 = (int) t.triangle[1].x;
        int x3 = (int) t.triangle[2].x;

        int y1 = (int) t.triangle[0].y;
        int y2 = (int) t.triangle[1].y;
        int y3 = (int) t.triangle[2].y;


        if (y2 == y3) {
            fillBottomFlatTriangle(c, x1, y1, x2, y2, x3, y3);
        } else if (y1 == y2) {
            fillTopFlatTriangle(c, x1, y1, x2, y2, x3, y3);
        } else {

            double x4 = x1 + ((double) (y2 - y1) / (double) (y3 - y1)) * (x3 - x1);
            fillBottomFlatTriangle(c, x1, y1, x2, y2, x4, y2);
            fillTopFlatTriangle(c, x2, y2, x4, y2, x3, y3);

        }
    }

    public void renderMesh(double yawX, double yawY, double camMoveX, double camMoveY, double camMoveZ) {

        ArrayList<Triangle> triangleList = new ArrayList<>();

        vCam.x += camMoveX;
        vCam.y += camMoveY;

        Matrix4x4 matTranslation = Matrix4x4.makeTranslation(0, 0, 10);

        Matrix4x4 matWorld = Matrix4x4.makeIdentity();
        matWorld = matWorld.multiplyMatrix(matTranslation);

        Vec3D vUp = new Vec3D(0, 1, 0);
        Vec3D vTarget = new Vec3D(0, 0, 1);

        Vec3D vForward = vLookDir.multiply(camMoveZ);
        vCam = vCam.add(vForward);
        Matrix4x4 matCameraRotY = Matrix4x4.makeRotationY(yawY);
        Matrix4x4 matCameraRot = Matrix4x4.makeRotationX(yawX);
        matCameraRot = matCameraRot.multiplyMatrix(matCameraRotY);
        vLookDir = matCameraRot.multiplyByVec3D(vTarget);
        vTarget = vCam.add(vLookDir);
        Matrix4x4 matCam = Matrix4x4.makePointAt(vCam, vTarget, vUp);
        Matrix4x4 matView = matCam.quickInverse();

        for (Triangle t : m.mesh) {

            Triangle triProjected, triTransformed, triViewed;

            triTransformed = new Triangle();
            triTransformed.triangle[0] = matWorld.multiplyByVec3D(t.triangle[0]);
            triTransformed.triangle[1] = matWorld.multiplyByVec3D(t.triangle[1]);
            triTransformed.triangle[2] = matWorld.multiplyByVec3D(t.triangle[2]);


            Vec3D line1 = triTransformed.triangle[1].sub(triTransformed.triangle[0]);
            Vec3D line2 = triTransformed.triangle[2].sub(triTransformed.triangle[0]);

            Vec3D normal = Vec3D.crossProduct(line1, line2);

            normal = normal.normalise();

            Vec3D cameraRay = triTransformed.triangle[0].sub(vCam);


            if (Vec3D.dotProduct(normal, cameraRay) < 0) {

                //Illumination
                Vec3D lightNormal = light.normalise();

                double dp = Math.max(Vec3D.dotProduct(lightNormal, normal), 0.1) * 255;
                if (dp < 0) dp = 0;
                Color c = new Color((int) dp, (int) dp, 0);


                triViewed = new Triangle();
                triViewed.triangle[0] = matView.multiplyByVec3D(triTransformed.triangle[0]);
                triViewed.triangle[1] = matView.multiplyByVec3D(triTransformed.triangle[1]);
                triViewed.triangle[2] = matView.multiplyByVec3D(triTransformed.triangle[2]);

                ArrayList<Triangle> clipped = triViewed.clipAgainstPlane(new Vec3D(0, 0, 0.1), new Vec3D(0, 0, 1));

                for (Triangle tClipped : clipped) {
                    //Project triangles 3D -> 2D
                    triProjected = new Triangle();
                    triProjected.triangle[0] = matProj.multiplyByVec3D(tClipped.triangle[0]);
                    triProjected.triangle[1] = matProj.multiplyByVec3D(tClipped.triangle[1]);
                    triProjected.triangle[2] = matProj.multiplyByVec3D(tClipped.triangle[2]);

                    triProjected.triangle[0] = triProjected.triangle[0].divide(triProjected.triangle[0].w);
                    triProjected.triangle[1] = triProjected.triangle[1].divide(triProjected.triangle[1].w);
                    triProjected.triangle[2] = triProjected.triangle[2].divide(triProjected.triangle[2].w);

                    triProjected.triangle[0].x *= -1.0f;
                    triProjected.triangle[1].x *= -1.0f;
                    triProjected.triangle[2].x *= -1.0f;
                    triProjected.triangle[0].y *= -1.0f;
                    triProjected.triangle[1].y *= -1.0f;
                    triProjected.triangle[2].y *= -1.0f;

                    Vec3D offsetView = new Vec3D(1, 1, 0);
                    triProjected.triangle[0] = triProjected.triangle[0].add(offsetView);
                    triProjected.triangle[1] = triProjected.triangle[1].add(offsetView);
                    triProjected.triangle[2] = triProjected.triangle[2].add(offsetView);

                    triProjected.triangle[0].x *= 0.5 * (double) width;
                    triProjected.triangle[1].x *= 0.5 * (double) width;
                    triProjected.triangle[2].x *= 0.5 * (double) width;
                    triProjected.triangle[0].y *= 0.5 * (double) height;
                    triProjected.triangle[1].y *= 0.5 * (double) height;
                    triProjected.triangle[2].y *= 0.5 * (double) height;


                    triProjected.color = c;
                    triangleList.add(triProjected);
                }
            }
        }

        triangleList.sort(new TriangleComparator());

        for (Triangle tToRaster : triangleList) {

            ArrayList<Triangle> listTriangles = new ArrayList<>();
            listTriangles.add(tToRaster);
            int nNewTriangles = 1;

            for (int p = 0; p < 4; p++) {
                while (nNewTriangles > 0) {
                    Triangle test = listTriangles.get(0);
                    listTriangles.remove(0);
                    nNewTriangles--;

                    ArrayList<Triangle> newTriangles = switch (p) {
                        case 0 -> test.clipAgainstPlane(new Vec3D(0, 0, 0), new Vec3D(0, 1, 0));
                        case 1 -> test.clipAgainstPlane(new Vec3D(0, height - 1, 0), new Vec3D(0, -1, 0));
                        case 2 -> test.clipAgainstPlane(new Vec3D(0, 0, 0), new Vec3D(1, 0, 0));
                        case 3 -> test.clipAgainstPlane(new Vec3D(width - 1, 0, 0), new Vec3D(-1, 0, 0));
                        default -> new ArrayList<>();
                    };

                    listTriangles.addAll(newTriangles);

                }
                nNewTriangles = listTriangles.size();

            }

            for (Triangle t : listTriangles) {
                paintTriangle(t.color, t);
//            drawTriangle(Color.BLACK, t);
            }


        }
    }

    public Vec3D MultiplyMatrixVector(Vec3D v, Matrix4x4 matrix) {
        double xout = v.x * matrix.m[0][0] + v.y * matrix.m[1][0] + v.z * matrix.m[2][0] + matrix.m[3][0];
        double yout = v.x * matrix.m[0][1] + v.y * matrix.m[1][1] + v.z * matrix.m[2][1] + matrix.m[3][1];
        double zout = v.x * matrix.m[0][2] + v.y * matrix.m[1][2] + v.z * matrix.m[2][2] + matrix.m[3][2];

        double w = v.x * matrix.m[0][3] + v.y * matrix.m[1][3] + v.z * matrix.m[2][3] + matrix.m[3][3];
        if (w != 0) {
            xout /= w;
            yout /= w;
            zout /= w;
        }
        return new Vec3D(xout, yout, zout);
    }

    public static Mesh loadObjFile(String path) {
        BufferedReader reader;
        ArrayList<Triangle> tList = new ArrayList<>();

        try {
            reader = new BufferedReader(new FileReader(
                    path));
            String line = reader.readLine();
            ArrayList<Vec3D> vList = new ArrayList<>();
            while (line != null) {
                String[] arr = line.split(" ");

                if (arr[0].equals("v")) {
                    vList.add(new Vec3D(Double.parseDouble(arr[1]), Double.parseDouble(arr[2]), Double.parseDouble(arr[3])));
                }

                if (arr[0].equals("f")) {
                    tList.add(new Triangle(vList.get(Integer.parseInt(arr[1]) - 1), vList.get(Integer.parseInt(arr[2]) - 1), vList.get(Integer.parseInt(arr[3]) - 1)));
                }


                line = reader.readLine();
            }
            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Mesh(tList.toArray(new Triangle[0]));
    }
}