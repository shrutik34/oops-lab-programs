import java.awt.*;
import java.util.Scanner;
import javax.swing.*;

public class CircleDrawer extends JPanel {
    enum Algo { DDA, BRESENHAM, MIDPOINT }
    enum Style { SOLID, DOTTED, HASHED }

    private Algo algo = Algo.DDA;
    private Style style = Style.SOLID;
    private int xc = 0, yc = 0, r = 0;
    private boolean ready = false;

    public void setParams(Algo algo, Style style, int xc, int yc, int r) {
        this.algo = algo;
        this.style = style;
        this.xc = xc;
        this.yc = yc;
        this.r = r;
        this.ready = true;
        repaint();
    }
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (!ready) return;
        int w = getWidth(), h = getHeight();
        int cx = w/2 + xc;
        int cy = h/2 - yc;

        switch (algo) {
            case DDA:
                drawDDACircle(g, cx, cy, r);
                break;
            case BRESENHAM:
                drawBresenhamCircle(g, cx, cy, r);
                break;
            case MIDPOINT:
                drawMidpointCircle(g, cx, cy, r);
                break;
        }
    }

    private void plot8(Graphics g, int x, int y, int cx, int cy, int count) {
        boolean draw = false;
        switch (style) {
            case SOLID:
                draw = true;
                break;
            case DOTTED:
                draw = (count % 4 == 0);
                break;
            case HASHED:
                draw = (count % 8 < 4);
                break;
        }
        if (!draw) return;

        g.drawLine(cx + x, cy + y, cx + x, cy + y);
        g.drawLine(cx - x, cy + y, cx - x, cy + y);
        g.drawLine(cx + x, cy - y, cx + x, cy - y);
        g.drawLine(cx - x, cy - y, cx - x, cy - y);
        g.drawLine(cx + y, cy + x, cx + y, cy + x);
        g.drawLine(cx - y, cy + x, cx - y, cy + x);
        g.drawLine(cx + y, cy - x, cx + y, cy - x);
        g.drawLine(cx - y, cy - x, cx - y, cy - x);
    }

    private void drawMidpointCircle(Graphics g, int cx, int cy, int radius) {
        int x = 0;
        int y = radius;
        int p = 1 - radius;
        int count = 0;

        while (x <= y) {
            plot8(g, x, y, cx, cy, count++);
            x++;
            if (p < 0) {
                p += 2*x + 1;
            } else {
                y--;
                p += 2*(x - y) + 1;
            }
        }
    }

    private void drawBresenhamCircle(Graphics g, int cx, int cy, int radius) {
        int x = 0, y = radius;
        int d = 3 - 2 * radius;
        int count = 0;

        while (x <= y) {
            plot8(g, x, y, cx, cy, count++);
            if (d <= 0) {
                d = d + 4*x + 6;
            } else {
                d = d + 4*(x - y) + 10;
                y--;
            }
            x++;
        }
    }

    private void drawDDACircle(Graphics g, int cx, int cy, int radius) {
        double theta = 0.0;
        double end = 2 * Math.PI;
        int count = 0;
        double step = 1.0 / radius;

        while (theta <= end) {
            int x = (int)Math.round(radius * Math.cos(theta));
            int y = (int)Math.round(radius * Math.sin(theta));
            plot8(g, x, y, cx, cy, count++);
            theta += step;
        }
    }
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Circle Drawing Menu");
        System.out.println("1. DDA");
        System.out.println("2. Bresenham");
        System.out.println("3. Midpoint");
        System.out.print("Choose algorithm (1-3): ");
        int a = sc.nextInt();

        CircleDrawer.Algo algo = CircleDrawer.Algo.DDA;
        if (a == 2) algo = CircleDrawer.Algo.BRESENHAM;
        else if (a == 3) algo = CircleDrawer.Algo.MIDPOINT;

        System.out.print("Enter center coordinates (xc yc): ");
        int xc = sc.nextInt();
        int yc = sc.nextInt();
        System.out.print("Enter radius: ");
        int r = sc.nextInt();

        System.out.println("Styles:");
        System.out.println("1. Solid");
        System.out.println("2. Dotted");
        System.out.println("3. Hashed");
        System.out.print("Choose style (1-3): ");
        int s = sc.nextInt();
        CircleDrawer.Style style = CircleDrawer.Style.SOLID;
        if (s == 2) style = CircleDrawer.Style.DOTTED;
        else if (s == 3) style = CircleDrawer.Style.HASHED;

        JFrame frame = new JFrame("Circle Drawer");
        CircleDrawer panel = new CircleDrawer();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        frame.setSize(600, 600);
        frame.setVisible(true);

        panel.setParams(algo, style, xc, yc, r);
    }
}

