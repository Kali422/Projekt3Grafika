public class Matrix4x4 {
    public double[][] m;

    public Matrix4x4() {
        m = new double[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                m[i][j] = 0;
            }
        }
    }

    public Vec3D multiplyByVec3D(Vec3D v) {
        double x = v.x * m[0][0] + v.y * m[1][0] + v.z * m[2][0] + v.w * m[3][0];
        double y = v.x * m[0][1] + v.y * m[1][1] + v.z * m[2][1] + v.w * m[3][1];
        double z = v.x * m[0][2] + v.y * m[1][2] + v.z * m[2][2] + v.w * m[3][2];
        double w = v.x * m[0][3] + v.y * m[1][3] + v.z * m[2][3] + v.w * m[3][3];
        return new Vec3D(x, y, z, w);
    }

    public static Matrix4x4 makeIdentity() {
        Matrix4x4 m = new Matrix4x4();
        m.m[0][0] = 1;
        m.m[1][1] = 1;
        m.m[2][2] = 1;
        m.m[3][3] = 1;
        return m;
    }

    public static Matrix4x4 makeRotationX(double angleRad) {
        Matrix4x4 m = new Matrix4x4();
        m.m[0][0] = 1;
        m.m[1][1] = Math.cos(angleRad);
        m.m[1][2] = Math.sin(angleRad);
        m.m[2][1] = -Math.sin(angleRad);
        m.m[2][2] = Math.cos(angleRad);
        m.m[3][3] = 1;
        return m;
    }

    public static Matrix4x4 makeRotationY(double angleRad) {
        Matrix4x4 m = new Matrix4x4();
        m.m[0][0] = Math.cos(angleRad);
        m.m[2][0] = -Math.sin(angleRad);
        m.m[1][1] = 1;
        m.m[0][2] = Math.sin(angleRad);
        m.m[2][2] = Math.cos(angleRad);
        m.m[3][3] = 1;
        return m;
    }

    public static Matrix4x4 makeRotationZ(double angleRad) {
        Matrix4x4 m = new Matrix4x4();
        m.m[0][0] = Math.cos(angleRad);
        m.m[0][1] = Math.sin(angleRad);
        m.m[1][0] = -Math.sin(angleRad);
        m.m[1][1] = Math.cos(angleRad);
        m.m[2][2] = 1;
        m.m[3][3] = 1;
        return m;

    }

    public static Matrix4x4 makeTranslation(double x, double y, double z) {
        Matrix4x4 m = new Matrix4x4();
        m.m[0][0] = 1;
        m.m[1][1] = 1;
        m.m[2][2] = 1;
        m.m[3][3] = 1;
        m.m[3][0] = x;
        m.m[3][1] = y;
        m.m[3][2] = z;
        return m;
    }

    public static Matrix4x4 makeProjection(double fovDegrees, double aspectRatio, double near, double far) {
        Matrix4x4 m = new Matrix4x4();
        double fovRad = 1 / Math.tan(fovDegrees * 0.5 / 180 * Math.PI);
        m.m[0][0] = aspectRatio * fovRad;
        m.m[1][1] = fovRad;
        m.m[2][2] = far / (far - near);
        m.m[3][2] = (-far * near) / (far - near);
        m.m[2][3] = 1;
        m.m[3][3] = 0;
        return m;
    }

    public static Matrix4x4 makePointAt(Vec3D pos, Vec3D target, Vec3D up) {
        Vec3D newForward = target.sub(pos);
        newForward = newForward.normalise();

        Vec3D a = newForward.multiply(Vec3D.dotProduct(up, newForward));
        Vec3D newUp = up.sub(a);
        newUp = newUp.normalise();

        Vec3D newRight = Vec3D.crossProduct(newUp, newForward);

        Matrix4x4 m = new Matrix4x4();
        m.m[0][0] = newRight.x;
        m.m[0][1] = newRight.y;
        m.m[0][2] = newRight.z;
//        m.m[0][3] = 0;
        m.m[1][0] = newUp.x;
        m.m[1][1] = newUp.y;
        m.m[1][2] = newUp.z;
//        m.m[1][3] = 0;
        m.m[2][0] = newForward.x;
        m.m[2][1] = newForward.y;
        m.m[2][2] = newForward.z;
//        m.m[2][3] = 0;
        m.m[3][0] = pos.x;
        m.m[3][1] = pos.y;
        m.m[3][2] = pos.z;
        m.m[3][3] = 1;
        return m;
    }

    public Matrix4x4 quickInverse() {
        Matrix4x4 matrix = new Matrix4x4();
        matrix.m[0][0] = m[0][0];
        matrix.m[0][1] = m[1][0];
        matrix.m[0][2] = m[2][0];
        matrix.m[1][0] = m[0][1];
        matrix.m[1][1] = m[1][1];
        matrix.m[1][2] = m[2][1];
        matrix.m[2][0] = m[0][2];
        matrix.m[2][1] = m[1][2];
        matrix.m[2][2] = m[2][2];
        matrix.m[3][0] = -(m[3][0] * matrix.m[0][0] + m[3][1] * matrix.m[1][0] + m[3][2] * matrix.m[2][0]);
        matrix.m[3][1] = -(m[3][0] * matrix.m[0][1] + m[3][1] * matrix.m[1][1] + m[3][2] * matrix.m[2][1]);
        matrix.m[3][2] = -(m[3][0] * matrix.m[0][2] + m[3][1] * matrix.m[1][2] + m[3][2] * matrix.m[2][2]);
        matrix.m[3][3] = 1;
        return matrix;
    }

    public Matrix4x4 multiplyMatrix(Matrix4x4 min) {
        Matrix4x4 mout = new Matrix4x4();
        for (int c = 0; c < 4; c++)
            for (int r = 0; r < 4; r++)
                mout.m[r][c] = m[r][0] * min.m[0][c] + m[r][1] * min.m[1][c] + m[r][2] * min.m[2][c] + m[r][3] * min.m[3][c];
        return mout;
    }
}