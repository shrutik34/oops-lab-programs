import java.util.Scanner;

public class CohenSutherland {
    static final int INSIDE = 0; 
    static final int LEFT = 1;   
    static final int RIGHT = 2;  
    static final int BOTTOM = 4; 
    static final int TOP = 8;    

    static double xmin, ymin, xmax, ymax;

   
    static int computeCode(double x, double y) {
        int code = INSIDE;

        if (x < xmin)
            code |= LEFT;
        else if (x > xmax)
            code |= RIGHT;
        if (y < ymin)
            code |= BOTTOM;
        else if (y > ymax)
            code |= TOP;

        return code;
    }
    static boolean cohenSutherlandClip(Line line) {
        double x1 = line.x1;
        double y1 = line.y1;
        double x2 = line.x2;
        double y2 = line.y2;

        int code1 = computeCode(x1, y1);
        int code2 = computeCode(x2, y2);

        boolean accept = false;

        while (true) {
            if ((code1 | code2) == 0) {
               
                accept = true;
                break;
            } else if ((code1 & code2) != 0) {
              
                break;
            } else {
                int codeOut;
                double x = 0, y = 0;

               
                codeOut = (code1 != 0) ? code1 : code2;

                if ((codeOut & TOP) != 0) {
                    x = x1 + (x2 - x1) * (ymax - y1) / (y2 - y1);
                    y = ymax;
                } else if ((codeOut & BOTTOM) != 0) {
                    x = x1 + (x2 - x1) * (ymin - y1) / (y2 - y1);
                    y = ymin;
                } else if ((codeOut & RIGHT) != 0) {
                    y = y1 + (y2 - y1) * (xmax - x1) / (x2 - x1);
                    x = xmax;
                } else if ((codeOut & LEFT) != 0) {
                    y = y1 + (y2 - y1) * (xmin - x1) / (x2 - x1);
                    x = xmin;
                }

                
                if (codeOut == code1) {
                    x1 = x;
                    y1 = y;
                    code1 = computeCode(x1, y1);
                } else {
                    x2 = x;
                    y2 = y;
                    code2 = computeCode(x2, y2);
                }
            }
        }

        if (accept) {
            line.x1 = x1;
            line.y1 = y1;
            line.x2 = x2;
            line.y2 = y2;
            return true;
        }

        return false;
    }

   
    static class Line {
        double x1, y1, x2, y2;

        Line(double x1, double y1, double x2, double y2) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

      
        xmin = 10;
        ymin = 10;
        xmax = 200;
        ymax = 150;

        System.out.println("Clipping window: (" + xmin + ", " + ymin + ") to (" + xmax + ", " + ymax + ")");
        System.out.print("Enter line endpoints (x1 y1 x2 y2): ");
        double x1 = scanner.nextDouble();
        double y1 = scanner.nextDouble();
        double x2 = scanner.nextDouble();
        double y2 = scanner.nextDouble();

        Line line = new Line(x1, y1, x2, y2);

        boolean visible = cohenSutherlandClip(line);

        if (visible) {
            System.out.printf("Line after clipping: (%.2f, %.2f) to (%.2f, %.2f)%n",
                    line.x1, line.y1, line.x2, line.y2);
        } else {
            System.out.println("Line lies completely outside the clipping window.");
        }

        scanner.close();
    }
}
