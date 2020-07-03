import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


class RunApp extends JFrame implements KeyListener {

    private final Draw draw;
    private double camMoveX = 0;
    private double camMoveY = 0;
    private double camMoveZ = 0;
    private double yawX = 0;
    private double yawY = 0;

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            camMoveY = -0.1;
            draw.fillCanvas(Color.WHITE);
            draw.renderMesh(yawX, yawY, camMoveX, camMoveY, camMoveZ);
            camMoveY = 0;
            repaint();
        }

        if (e.getKeyCode() == KeyEvent.VK_UP) {
            camMoveY = 0.1;
            draw.fillCanvas(Color.WHITE);
            draw.renderMesh(yawX, yawY, camMoveX, camMoveY, camMoveZ);
            camMoveY = 0;
            repaint();
        }

        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            camMoveX += 0.1;
            draw.fillCanvas(Color.WHITE);
            draw.renderMesh(yawX, yawY, camMoveX, camMoveY, camMoveZ);
            camMoveX = 0;
            repaint();
        }

        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            camMoveX -= 0.1;
            draw.fillCanvas(Color.WHITE);
            draw.renderMesh(yawX, yawY, camMoveX, camMoveY, camMoveZ);
            camMoveX = 0;
            repaint();
        }

        if (e.getKeyCode() == KeyEvent.VK_A) {
            yawY -= 0.05;
            draw.fillCanvas(Color.WHITE);
            draw.renderMesh(yawX, yawY, camMoveX, camMoveY, camMoveZ);
            repaint();
        }

        if (e.getKeyCode() == KeyEvent.VK_D) {
            yawY += 0.05;
            draw.fillCanvas(Color.WHITE);
            draw.renderMesh(yawX, yawY, camMoveX, camMoveY, camMoveZ);
            repaint();
        }

        if (e.getKeyCode() == KeyEvent.VK_W) {
            camMoveZ = 0.1;
            draw.fillCanvas(Color.WHITE);
            draw.renderMesh(yawX, yawY, camMoveX, camMoveY, camMoveZ);
            repaint();
        }

        if (e.getKeyCode() == KeyEvent.VK_S) {
            camMoveZ = -0.1;
            draw.fillCanvas(Color.WHITE);
            draw.renderMesh(yawX, yawY, camMoveX, camMoveY, camMoveZ);
            repaint();
        }

        if (e.getKeyCode() == KeyEvent.VK_P) {
            yawX -= 0.05;
            draw.fillCanvas(Color.WHITE);
            draw.renderMesh(yawX, yawY, camMoveX, camMoveY, camMoveZ);
            repaint();
        }

        if (e.getKeyCode() == KeyEvent.VK_L) {
            yawX += 0.05;
            draw.fillCanvas(Color.WHITE);
            draw.renderMesh(yawX, yawY, camMoveX, camMoveY, camMoveZ);
            repaint();
        }

        camMoveZ = 0;

    }

    @Override
    public void keyReleased(KeyEvent e) {


    }

    public RunApp(int width, int height) {
        Mesh mesh = Draw.loadObjFile("scene.obj");
        this.draw = new Draw(mesh, width, height);
        this.draw.renderMesh(0, 0, 0, 0, 0);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                int width = 640;
                int height = 520;
                RunApp frame = new RunApp(width, height);
                frame.setTitle("Cube Animation");
                frame.setResizable(false);
                frame.setSize(width, height);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.getContentPane().add(frame.draw);
                frame.pack();
                frame.setVisible(true);
            }
        });
    }
}
