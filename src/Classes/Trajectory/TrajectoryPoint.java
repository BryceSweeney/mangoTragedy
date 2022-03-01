package Classes.Trajectory;

import Classes.General.Point;

public class TrajectoryPoint {
    public double posX;
    public double posY;
    public double posTheta;

    public TrajectoryPoint(double posX, double posY, double posTheta) {
        this.posX = posX;
        this.posY = posY;
        this.posTheta = posTheta;
    }

    public TrajectoryPoint(Point point, double posTheta) {
        this.posX = point.x;
        this.posY = point.y;
        this.posTheta = posTheta;
    }

    public Point toPoint() {
        return new Point(posX, posY);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "{" + posX + ", " + posY + ", " + posTheta + "}";
    }
}