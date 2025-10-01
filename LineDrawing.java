import javax.swing.*;
import java.awt.*;

public class LineDrawing extends JPanel {

    // ---------- DDA Line ----------
    void drawDDA(Graphics g, int x1, int y1, int x2, int y2, String style) {
        Graphics2D g2d = (Graphics2D) g;
        int dx = x2 - x1, dy = y2 - y1;
        int steps = Math.max(Math.abs(dx), Math.abs(dy));
        float xInc = dx / (float) steps;
        float yInc = dy / (float) steps;
        float x = x1, y = y1;

        for (int i = 0; i <= steps; i++) {
            if (style.equals("dotted")) {
                if (i % 2 == 0) g2d.drawLine(Math.round(x), Math.round(y), Math.round(x), Math.round(y));
            } else if (style.equals("thick")) {
                g2d.drawLine(Math.round(x), Math.round(y), Math.round(x) + 1, Math.round(y));
                g2d.drawLine(Math.round(x), Math.round(y), Math.round(x), Math.round(y) + 1);
            } else {
                g2d.drawLine(Math.round(x), Math.round(y), Math.round(x), Math.round(y));
            }
            x += xInc;
            y += yInc;
        }
    }

    // ---------- Bresenham Line ----------
    void drawBresenham(Graphics g, int x1, int y1, int x2, int y2, String style) {
        Graphics2D g2d = (Graphics2D) g;
        int dx = Math.abs(x2 - x1), dy = Math.abs(y2 - y1);
        int sx = (x1 < x2) ? 1 : -1;
        int sy = (y1 < y2) ? 1 : -1;
        int err = dx - dy, e2;
        int count = 0;

        while (true) {
            if (style.equals("dashed")) {
                if (count % 10 < 5) g2d.drawLine(x1, y1, x1, y1);
            } else {
                g2d.drawLine(x1, y1, x1, y1);
            }
            if (x1 == x2 && y1 == y2) break;
            e2 = 2 * err;
            if (e2 > -dy) { err -= dy; x1 += sx; }
            if (e2 < dx) { err += dx; y1 += sy; }
            count++;
        }
    }

    // ---------- Pattern Drawing ----------
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Outer rectangle (DDA dotted)
        drawDDA(g, 100, 100, 300, 100, "dotted");
        drawDDA(g, 300, 100, 300, 300, "dotted");
        drawDDA(g, 300, 300, 100, 300, "dotted");
        drawDDA(g, 100, 300, 100, 100, "dotted");

        // Inner rectangle (DDA thick)
        drawDDA(g, 150, 150, 250, 150, "thick");
        drawDDA(g, 250, 150, 250, 250, "thick");
        drawDDA(g, 250, 250, 150, 250, "thick");
        drawDDA(g, 150, 250, 150, 150, "thick");

        // Diamond (Bresenham dashed + solid)
        drawBresenham(g, 200, 80, 320, 200, "dashed");  // top-right
        drawBresenham(g, 200, 80, 80, 200, "solid");   // top-left
        drawBresenham(g, 80, 200, 200, 320, "dashed"); // bottom-left
        drawBresenham(g, 320, 200, 200, 320, "solid"); // bottom-right
    }

    // ---------- Main ----------
    public static void main(String[] args) {
        JFrame frame = new JFrame("Line Drawing Algorithms");
        LineDrawing panel = new LineDrawing();
        frame.add(panel);
        frame.setSize(450, 450);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
