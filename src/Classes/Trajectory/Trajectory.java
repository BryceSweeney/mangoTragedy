package Classes.Trajectory;

import Classes.Exceptions.NodeTimeException;
import Classes.General.Point;
import Classes.General.Util;

public class Trajectory {
    // Path Parameters
    // Note that each point is a tick apart from the last (1/50th of a second) 
    public TrajectoryPoint[] pathPoints; // Array of path points

    // Trajectory Nodes
    public TrajectoryNode[] pathNodes; // Array of path nodes
    

    public Trajectory(TrajectoryNode[] pathNodes) {
        this.pathNodes = pathNodes;
        double maxTime = 0;
        for (TrajectoryNode trajectoryNode : pathNodes) {
            if (trajectoryNode.time > maxTime) maxTime = trajectoryNode.time;
        }
        this.pathPoints = new TrajectoryPoint[(int) maxTime];
    }

    @SuppressWarnings("unused")
    public void generate() throws NodeTimeException {
        pathPoints = new TrajectoryPoint[(int) pathNodes[pathNodes.length - 1].time + 1];
        for (int i = 0; i < pathNodes.length - 1; i++) {
            Point[] newPath = generateBazierCurve(pathNodes[i], pathNodes[i + 1]);
            TrajectoryPoint[] newSubpath = new TrajectoryPoint[newPath.length];

            int iteration = 0;
            for (TrajectoryPoint ignored : newSubpath) {
                newSubpath[iteration] = new TrajectoryPoint(newPath[iteration], Util.smoothTheta(pathNodes[i], pathNodes[i + 1], iteration));
                iteration++;
            }
                
            iteration = 0;
            for (int j = (int) pathNodes[i].time; j < pathNodes[i + 1].time; j++) {
                pathPoints[j] = newSubpath[iteration];
                iteration++;
            }
        }
        pathPoints[pathPoints.length - 1] = new TrajectoryPoint(pathNodes[pathNodes.length - 1].toPoint(), pathNodes[pathNodes.length - 1].dirTheta);
    }

    public static Point[] generateBazierCurve(TrajectoryNode nodeA, TrajectoryNode nodeB) throws NodeTimeException {
        // Generating secondary points via the input node's position, theta, and magnitude
        Point vectorA = new Point(nodeA.magnitude * Math.cos(Util.toRadians(nodeA.posTheta)) + nodeA.posX,
                                    nodeA.magnitude * Math.sin(Util.toRadians(nodeA.posTheta)) + nodeA.posY);
        Point vectorB = new Point(nodeB.magnitude * -Math.cos(Util.toRadians(nodeB.posTheta)) + nodeB.posX,
                                    nodeB.magnitude * -Math.sin(Util.toRadians(nodeB.posTheta)) + nodeB.posY);
        

        int pointCount = (int) (nodeB.time - nodeA.time); // Generating the amount of points in between the nodes
        if (pointCount <= 0) {                            // Keep in mind that points are always one timestep away from each other
            throw new NodeTimeException();
        }
        Point[] pointCurve = new Point[pointCount]; // Creating the array of points

        int iteration = 0; // Generating the points on the curve via the baizer formula
        for (double t = 0; t < 1; t += 1/(pointCount * 1d)) {
            // Baizer formula
            try {
                pointCurve[iteration] = Util.sum(Util.scale(Math.pow(1 - t, 3), nodeA.toPoint()), // Math based on first node
                                                    Util.scale(3 * t * Math.pow(1 - t, 2), vectorA), // Math based on first node's vector
                                                    Util.scale(3 * Math.pow(t, 2) * (1 - t), vectorB), // Math based on second node's vector
                                                    Util.scale(Math.pow(t, 3), nodeB.toPoint())); // Math based on second node
            } catch (ArrayIndexOutOfBoundsException ignored) {}
            iteration++;
        }

        return pointCurve;
    }
}
