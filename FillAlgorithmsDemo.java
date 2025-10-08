import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class FillAlgorithmsDemo extends Frame implements MouseListener, KeyListener, ActionListener {

    // Modes
    private static final int NONE = 0, FLOOD = 1, BOUNDARY = 2, SCANLINE = 3;
    private int mode = NONE;

    // Common drawing area
    int width = 500, height = 500;
    int[][] pixels = new int[width][height]; // used for flood and boundary

    // Scanline polygon
    ArrayList<Point> points = new ArrayList<>();
    boolean scanlineFill = false;

    public FillAlgorithmsDemo() {
        super("Fill Algorithms Demo");
        setSize(width, height);
        setLocationRelativeTo(null);
        addMouseListener(this);
        addKeyListener(this);

        // Menu
        MenuBar mb = new MenuBar();
        Menu menu = new Menu("Algorithms");
        MenuItem mFlood = new MenuItem("Flood Fill");
        MenuItem mBoundary = new MenuItem("Boundary Fill");
        MenuItem mScanline = new MenuItem("Scanline Fill");

        mFlood.addActionListener(this);
        mBoundary.addActionListener(this);
        mScanline.addActionListener(this);

        menu.add(mFlood);
        menu.add(mBoundary);
        menu.add(mScanline);
        mb.add(menu);
        setMenuBar(mb);

        // Close button
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
                System.exit(0);
            }
        });

        setVisible(true);
    }

    private void prepareRectangleBoundary() {
        // reset pixel array
        pixels = new int[width][height];
        for (int x = 100; x <= 300; x++) {
            pixels[x][100] = 1;
            pixels[x][300] = 1;
        }
        for (int y = 100; y <= 300; y++) {
            pixels[100][y] = 1;
            pixels[300][y] = 1;
        }
    }

    @Override
    public void paint(Graphics g) {
        if (mode == FLOOD || mode == BOUNDARY) {
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    if (pixels[x][y] == 1) {
                        g.setColor(Color.BLACK);
                        g.drawLine(x, y, x, y);
                    } else if (pixels[x][y] == 2) {
                        g.setColor(mode == FLOOD ? Color.RED : Color.BLUE);
                        g.drawLine(x, y, x, y);
                    }
                }
            }
        }

        if (mode == SCANLINE) {
            // draw polygon outline
            if (points.size() > 1) {
                g.setColor(Color.BLACK);
                for (int i = 0; i < points.size(); i++) {
                    Point p1 = points.get(i);
                    Point p2 = points.get((i + 1) % points.size());
                    g.drawLine(p1.x, p1.y, p2.x, p2.y);
                }
            }

            if (scanlineFill && points.size() >= 3) {
                doScanlineFill(g);
            }
        }
    }

    private void doScanlineFill(Graphics g) {
        int n = points.size();
        int[] px = new int[n];
        int[] py = new int[n];
        for (int i = 0; i < n; i++) {
            px[i] = points.get(i).x;
            py[i] = points.get(i).y;
        }

        int ymin = py[0], ymax = py[0];
        for (int i = 1; i < n; i++) {
            if (py[i] < ymin) ymin = py[i];
            if (py[i] > ymax) ymax = py[i];
        }

        g.setColor(Color.ORANGE);

        for (int y = ymin; y <= ymax; y++) {
            int[] nodes = new int[n];
            int nodesCount = 0;

            int j = n - 1;
            for (int i = 0; i < n; i++) {
                if ((py[i] < y && py[j] >= y) || (py[j] < y && py[i] >= y)) {
                    nodes[nodesCount++] = px[i] + (y - py[i]) * (px[j] - px[i]) / (py[j] - py[i]);
                }
                j = i;
            }

            // sort intersections
            for (int i = 0; i < nodesCount - 1; i++) {
                for (int k = i + 1; k < nodesCount; k++) {
                    if (nodes[i] > nodes[k]) {
                        int temp = nodes[i];
                        nodes[i] = nodes[k];
                        nodes[k] = temp;
                    }
                }
            }

            for (int i = 0; i < nodesCount; i += 2) {
                if (i + 1 < nodesCount) {
                    g.drawLine(nodes[i], y, nodes[i + 1], y);
                }
            }
        }
    }

    private void floodFill(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) return;
        if (pixels[x][y] != 0) return;
        pixels[x][y] = 2;
        floodFill(x + 1, y);
        floodFill(x - 1, y);
        floodFill(x, y + 1);
        floodFill(x, y - 1);
    }

    private void boundaryFill(int x, int y, int fillColor, int boundaryColor) {
        if (x < 0 || x >= width || y < 0 || y >= height) return;
        if (pixels[x][y] == boundaryColor || pixels[x][y] == fillColor) return;
        pixels[x][y] = fillColor;
        boundaryFill(x + 1, y, fillColor, boundaryColor);
        boundaryFill(x - 1, y, fillColor, boundaryColor);
        boundaryFill(x, y + 1, fillColor, boundaryColor);
        boundaryFill(x, y - 1, fillColor, boundaryColor);
    }

    // ========== Event handlers ==========

    @Override
    public void mouseClicked(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();

        if (mode == FLOOD) {
            floodFill(x, y);
        } else if (mode == BOUNDARY) {
            boundaryFill(x, y, 2, 1);
        } else if (mode == SCANLINE && !scanlineFill) {
            points.add(e.getPoint());
        }
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (mode == SCANLINE && e.getKeyCode() == KeyEvent.VK_F && points.size() >= 3) {
            scanlineFill = true;
            repaint();
        }
    }

    public void keyReleased(KeyEvent e) {}
    public void keyTyped(KeyEvent e) {}
    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if (cmd.equals("Flood Fill")) {
            mode = FLOOD;
            prepareRectangleBoundary();
        } else if (cmd.equals("Boundary Fill")) {
            mode = BOUNDARY;
            prepareRectangleBoundary();
        } else if (cmd.equals("Scanline Fill")) {
            mode = SCANLINE;
            points.clear();
            scanlineFill = false;
        }
        repaint();
    }

    public static void main(String[] args) {
        new FillAlgorithmsDemo();
    }
}
