package Classes.Trajectory;

import Classes.General.Point;

public class TrajectoryNode extends TrajectoryPoint {
    public double magnitude;
    public double dirTheta;
    public double time;

    public TrajectoryNode(double posX, double posY, double posTheta, double dirTheta, double magnitude, double time) {
        super(posX, posY, posTheta);
        this.dirTheta = dirTheta;
        this.magnitude = magnitude;
        this.time = time;
    }    

    @Override
    public TrajectoryNode clone() {
        return new TrajectoryNode(posX, posY, posTheta, dirTheta, magnitude, time);
    }

    public Point toPoint() {
        return new Point(posX, posY);
    }

    @Override
    public String toString() {
        return "{" + posX + ", " + posY + ", " + posTheta + ", " + dirTheta + ", " + magnitude + ", " + time + "}";
    }

    public static TrajectoryNode parseNode(String input) {
        String[] sepValues = input.split(",");
        if (sepValues.length != 6) throw new NumberFormatException();
        double[] parsedValues = new double[sepValues.length];
        for (int i = 0; i < parsedValues.length; i++) {
            sepValues[i].strip();
            switch (i) {
                case 0: sepValues[i] = sepValues[i].substring(1); break;
                case 5: sepValues[i] = sepValues[i].substring(0, sepValues[i].length() - 2); break;
            }
            parsedValues[i] = Double.parseDouble(sepValues[i]);
        }
        return new TrajectoryNode(parsedValues[0], parsedValues[1], parsedValues[2], 
                                    parsedValues[3], parsedValues[4], parsedValues[5]);
    }
}
