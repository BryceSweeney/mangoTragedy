package Classes.General;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.*;

import javax.imageio.ImageIO;
import javax.swing.*;

import Classes.Trajectory.TrajectoryNode;
import Classes.Trajectory.TrajectoryPoint;

public class Util {
    public static final int PPM = 73;
    public static final double roboWidth = 1.37;
    public static final double roboHeight = 0.86;

    /**
     * Finds the minimum value in an array of doubles
     * 
     * @param in Input Array
     * @return Maximum Value
     */
    public static double min(double[] in) {
        double min = in[0];
        for (double val : in) {
            if (val < min) min = val;
        }
        return min;
    }

    /**
     * Finds the maximum value in an array of doubles.
     * 
     * @param in Input Array
     * @return Maximum Value
     */
    public static double max(double[] in) {
        double max = in[0];
        for (double val : in) {
            if (val > max) max = val;
        }
        return max;
    }

    /**
     * Scales a point to a double value
     * Scaling a point means that both values are multiplied by the scalar value
     * 
     * @param scalar Scalar
     * @param point Input Point
     * @return Scaled Point Value
     */
    public static Point scale(double scalar, Point point) {
        return new Point(point.x * scalar, point.y * scalar);
    }

    /**
     * Sums together an arbitrary amount of points
     * When summing points, the individual values are added together
     * 
     * @param points Points
     * @return Sum of the Points
     */
    public static Point sum(Point... points) {
        double sumX = 0;
        double sumY = 0;
        for (Point point : points) {
            sumX += point.x;
            sumY += point.y;
        }
        return new Point(sumX, sumY);
    }

    /**
     * Converts a degree value into radians
     * Useful and generally necessary for use of Math.java's trigonometric functions
     * 
     * @param degrees Input degree value
     * @return Ouput radian value (same effective value)
     */
    public static double toRadians(double degrees) {
        return degrees / 57.2957795;
    }

    /**
     * Converts a radian value into degrees
     * Useful and generally necessary for use of Math.java's inverse trigonometric functions
     * 
     * @param radians
     * @return
     */
    public static double toDegrees(double radians) {
        return radians * 57.2957795;
    }

    public static void toWindowGraph(TrajectoryPoint[] points, Point[] vectorPoints) throws IOException {
        JFrame frame = new JFrame("tickGraph");
        BufferedImage fieldImage = ImageIO.read(new File("lib\\rsc\\field.png"));
        BufferedImage image = new BufferedImage(fieldImage.getWidth(), fieldImage.getHeight(), BufferedImage.TYPE_INT_ARGB);

        frame.setSize(fieldImage.getWidth(), fieldImage.getHeight());
        frame.setLocation(50, 50);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setAlwaysOnTop(true);

        Point[] translatedVectorPoints = vectorPoints.clone();
        for (int i = 0; i < translatedVectorPoints.length; i++) {
            translatedVectorPoints[i] = new Point(translatedVectorPoints[i].x * PPM + PPM, fieldImage.getHeight() - translatedVectorPoints[i].y * PPM - 30);
        }

        Color vectorColor = new Color(0, 200, 0, 255);
        for (int i = 0; i < translatedVectorPoints.length; i+=2) {
            drawVector(image, vectorColor, translatedVectorPoints[i], translatedVectorPoints[i+1]);
        }

        TrajectoryPoint[] translatedPoints = points.clone();
        for (int i = 0; i < translatedPoints.length; i++) {
            translatedPoints[i] = new TrajectoryPoint(translatedPoints[i].posX * PPM  + PPM, fieldImage.getHeight() - translatedPoints[i].posY * PPM - 30, points[i].posTheta);
        }

        int iteration = 0;
        for (TrajectoryPoint point : translatedPoints) {
            Color pointColor = new Color(200, 0, 200, 255);
            drawPoint(image, pointColor, point);
            if (iteration % 10 == 0) {
                pointColor = new Color(150, 0, 150, 80);
                drawRobot(image, pointColor, point.toPoint(), point.posTheta);
            }
            iteration++;
        }

        BufferedImage appendImage = new BufferedImage(fieldImage.getWidth(), fieldImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
        appendImage.getGraphics().drawImage(fieldImage, 0, 0, null);
        appendImage.getGraphics().drawImage(image, 0, 0, null);
        frame.getContentPane().add(new JLabel(new ImageIcon(appendImage)));
        frame.setVisible(true);
    }

    public static void drawPoint(BufferedImage image, Color color,  TrajectoryPoint point) {
        Graphics g = image.getGraphics();
        g.setColor(color);
        g.fillOval((int) Math.round(point.posX) - 2, (int) Math.round(point.posY) - 2, 3, 3);
        g.dispose();
    }

    public static void drawVector(BufferedImage image, Color color, Point pointA, Point pointB) {
        Graphics g = image.getGraphics();
        g.setColor(color);
        g.drawLine((int) Math.round(pointA.x), (int) Math.round(pointA.y), (int) Math.round(pointB.x), (int) Math.round(pointB.y));
        g.drawLine((int) Math.round(pointA.x), (int) Math.round(pointA.y) - 1, (int) Math.round(pointB.x), (int) Math.round(pointB.y) - 1);
        g.fillOval((int) Math.round(pointA.x) - 4, (int) Math.round(pointA.y) - 4, 8, 8);
        g.fillOval((int) Math.round(pointB.x) - 8, (int) Math.round(pointB.y) - 8, 16, 16);
        g.dispose();
    }

    public static void drawRobot(BufferedImage image, Color color, Point P, double theta) {
        Graphics g = image.getGraphics();
        g.setColor(color);

    //     Point A = new Point(0, 0);
    //     Point B = new Point(0, 0);
    //     Point C = new Point(0, 0);
    //     Point D = new Point(0, 0);

    //     double w = roboWidth * PPM;
    //     double h  = roboHeight * PPM;
    //     double L = 0.5 * Math.sqrt(w*w + h*h);

    //     double gamma = Math.asin(0.5 * h/L) + Math.toRadians(theta);
    //     A.x = P.x - L * Math.cos(gamma);
    //     A.y = P.y - L * Math.sin(gamma);
    //     D.x = P.x + L * Math.cos(gamma);
    //     D.y = P.y - L * Math.sin(gamma);
    //     B.x = P.x - L * Math.cos(gamma);
    //     B.y = P.y + L * Math.sin(gamma);
    //     C.x = P.x + L * Math.cos(gamma);
    //     C.y = P.y - L * Math.cos(gamma);

    //     // gamma = Math.asin(0.5 * h/L) - Math.toRadians(theta);

    //     g.drawLine((int) A.x, (int) A.y, (int) B.x, (int) B.y);
    //     g.drawLine((int) B.x, (int) B.y, (int) C.x, (int) C.y);
    //     g.drawLine((int) C.x, (int) C.y, (int) D.x, (int) D.y);
    //     g.drawLine((int) D.x, (int) D.y, (int) A.x, (int) A.y);

        g.drawLine((int) P.x, (int) P.y, (int) (P.x + 50 * Math.cos(Math.toRadians(-theta))), (int) (P.y + 50 * Math.sin(Math.toRadians(-theta))));
    }

    public static String toStringGraph(Point[] points, int width, int height, double xRange, double yRange, Point... POI) {
        StringBuilder s = new StringBuilder();
        for (int y = width; y > 0; y--) {
            for (int x = 0; x < height; x++) {
                int totalPoints = 0;
                boolean isPointOfInterest = false;
                for (Point point : points) {
                    if (point.x >= (xRange/width) * x && point.x < (xRange/width) * (x + 1) &&
                        point.y >= (yRange/height) * y && point.y < (yRange/height) * (y + 1)) totalPoints++;
                }
                for (Point point : POI) {
                    if (point.x >= (xRange/width) * x && point.x < (xRange/width) * (x + 1) &&
                        point.y >= (yRange/height) * y && point.y < (yRange/height) * (y + 1)) isPointOfInterest = true;
                }
                if (isPointOfInterest) { 
                    s.append("||");
                } else if (totalPoints > 0) {
                    s.append("██");
                } else {
                    s.append("  ");
                }
            }
            s.append('\n');
        }

        return s.toString();
    }

    public static double smoothTheta(TrajectoryNode nodeA, TrajectoryNode nodeB, double time) {
        double dOdt = (nodeB.dirTheta - nodeA.dirTheta) / (nodeB.time - nodeA.time);
        return nodeA.dirTheta + dOdt * time;
    }

    public static TrajectoryNode parseNode(String message) {
        String[] splitMessage = new String[6];
        Double[] parsedMessage = new Double[6];

        message.strip();
        splitMessage = message.split(",");

        for (int i = 0; i < 6; i++) {
            parsedMessage[i] = Double.parseDouble(splitMessage[i]);
        }

        return new TrajectoryNode(parsedMessage[0], parsedMessage[1], parsedMessage[2], parsedMessage[3], parsedMessage[4], parsedMessage[5]);
    }
}
