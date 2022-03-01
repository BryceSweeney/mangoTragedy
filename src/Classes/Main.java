package Classes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Classes.Exceptions.NodeTimeException;
import Classes.General.Point;
import Classes.General.Util;
import Classes.Trajectory.Trajectory;
import Classes.Trajectory.TrajectoryNode;
import Classes.Trajectory.TrajectoryPoint;

public class Main {
        public static void main(String[] args) throws NodeTimeException, JSONException, IOException {
        Scanner keyboard = new Scanner(System.in);
        ArrayList<TrajectoryNode> trajectoryNodeList = new ArrayList<>();
        TrajectoryNode[] trajectoryNodes;

        println();
        println("Welcome to PathGen, created by Bryce Sweeney. (2022)");
        println("Enter the command \"help\" for more information on the commands.");
        println();

        boolean loop = true;
        while (loop) {
            String input = keyboard.nextLine();
            switch (input.toLowerCase().split(" ")[0]) {
                case "help":
                    println();
                    println("Current commands include:");
                    println("\thelp - You're seeing it now.");
                    println("\tnodes - Prints the current nodes.");
                    println("\taddNode\t[x] [y] [positionTheta] [directionTheta] [magnitude] [timeStep] (index) - Generates a new node at the (last) index.");
                    println("\tsetNode\t[x] [y] [positionTheta] [directionTheta] [magnitude] [timeStep] [index] - Sets values of a node at a specified index.");
                    println("\tremoveNode [x] [y] [positionTheta] [directionTheta] [magnitude] [timeStep] - Removes a node in the list.");
                    println("\t\t   [index]");
                    println("\tgenerate - Generates a graph of the trajectory, nodes and vectors included.");
                    println("\tsave - Saves the points of the trajectory in a json file; an array of {x, y, theta} values.");
                    println("\tdelete - Deletes said path file.");
                    println("\texit - Exits the program (no way!)");
                    println();
                    break;
                case "nodes":
                    println();
                    if (trajectoryNodeList.size() == 0) {
                        println("There are no nodes in the list.");
                        println("(if you're seeing this just after loading, generate and it'll fix itself)");
                        stillRun();
                    } else {
                        println("Nodes: ");
                        for (int i = 0; i < trajectoryNodeList.size(); i++) {
                            println("\t" + i + " - " + trajectoryNodeList.get(i));
                        }
                    }
                    println();
                    break;
                case "addnode":
                    String[] command = input.split(" ");
                    double[] parsedValues = new double[command.length - 1];
                    if (parsedValues.length != 6 && parsedValues.length != 7) {
                        println();
                        println("Could not generate node due to invalid argument count. (6, 7 arguments)");
                        println("Please check the help command for more info.");
                        stillRun();
                        println();
                        break;
                    }
                    try {
                        for (int i = 0; i < parsedValues.length; i++) {
                            parsedValues[i] = Double.parseDouble(command[i + 1]);
                        }
                        if (parsedValues.length == 6) {
                            trajectoryNodeList.add(new TrajectoryNode(parsedValues[0], parsedValues[1], parsedValues[2], parsedValues[3], parsedValues[4], parsedValues[5]));

                            println();
                            println("Generated a new node. (Index = " + (trajectoryNodeList.size() - 1) + ")");
                            println();
                        } else if (parsedValues.length == 7) {
                            ArrayList<TrajectoryNode> subList = new ArrayList<>();
                            subList.add(new TrajectoryNode(parsedValues[0], parsedValues[1], parsedValues[2], parsedValues[3], parsedValues[4], parsedValues[5]));
                            for (int i = (int) parsedValues[6]; i < trajectoryNodeList.size(); i++) {
                                subList.add(trajectoryNodeList.get(i));
                            }
                            int iteration = 0;
                            int initSize = trajectoryNodeList.size();
                            for (int i = (int) parsedValues[6]; i <= initSize; i++) {
                                if (i == trajectoryNodeList.size()) {
                                    trajectoryNodeList.add(subList.get(iteration));
                                } else {
                                    trajectoryNodeList.set(i, subList.get(iteration));
                                }
                                iteration++;
                            }

                            println();
                            println("Set node (Index " + (int) parsedValues[6] + ") to new values.");
                            println();
                        }
                    } catch (NumberFormatException e) {
                        println();
                        e.printStackTrace();
                        println("Could not generate node due to NaN values. Stack trace printed above.");
                        stillRun();
                        println();
                        break;
                    }
                    break;
                case "setnode":
                    command = input.split(" ");
                    parsedValues = new double[command.length - 1];
                    if (parsedValues.length != 7) {
                        println();
                        println("Could not remove node due to invalid argument count. (7 arguments)");
                        println("Please check the help command for more info.");
                        stillRun();
                        println();
                        break;
                    }
                    try {
                        for (int i = 0; i < parsedValues.length; i++) {
                            parsedValues[i] = Double.parseDouble(command[i + 1]);
                        }
                        trajectoryNodeList.set((int) parsedValues[6], new TrajectoryNode(parsedValues[0], parsedValues[1], parsedValues[2], parsedValues[3], parsedValues[4], parsedValues[5]));

                        println();
                        println(String.format("Sucessfully replaced node (Index = %d) with new values.", (int) parsedValues[6]));
                        println();
                    } catch (NumberFormatException e) {
                        println();
                        e.printStackTrace();
                        println("Could not generate node due to NaN values. Stack trace printed above.");
                        stillRun();
                        println();
                        break;
                    }
                    break;
                case "removenode":
                    command = input.split(" ");
                    parsedValues = new double[command.length - 1];
                    if (parsedValues.length != 1 && parsedValues.length != 6) {
                        println();
                        println("Could not remove node due to invalid argument count. (1, 6 arguments)");
                        println("Please check the help command for more info.");
                        stillRun();
                        println();
                        break;
                    }
                    try {
                        for (int i = 0; i < parsedValues.length; i++) {
                            parsedValues[i] = Double.parseDouble(command[i + 1]);
                        }
                        if (parsedValues.length == 1) {
                            try {
                                trajectoryNodeList.remove((int) parsedValues[0]);
                                println();
                                println("Sucessfully removed node.");
                                println();
                            } catch (ArrayIndexOutOfBoundsException e) {
                                println();
                                println("Could not remove node of index " + parsedValues[1] + " because it does not exist.");
                                stillRun();
                                println();
                                break;
                            }
                        } else {
                            trajectoryNodeList.remove(new TrajectoryNode(parsedValues[0], parsedValues[1], parsedValues[2], parsedValues[3], parsedValues[4], parsedValues[5]));
                            println();
                            println("Sucessfully removed node.");
                            println();
                        }
                    } catch (NumberFormatException e) {
                        println();
                        e.printStackTrace();
                        println("Could not remove node due to NaN values. Stack trace printed above.");
                        stillRun();
                        println();
                        break;
                    }
                    break;
                case "generate":
                    trajectoryNodes = new TrajectoryNode[trajectoryNodeList.size()];
                    for (int i = 0; i < trajectoryNodes.length; i++) {
                        trajectoryNodes[i] = trajectoryNodeList.get(i);
                    } 

                    Trajectory trajectory = new Trajectory(trajectoryNodes);
                    try {
                        trajectory.generate();
                    } catch (NodeTimeException e) {
                        println();
                        println("Your nodes threw a node time exception. Maybe try and see if theres an issue with the times?");
                        println("If a node has a higher time than the one in front of it, or if theres two nodes with the same time, an error will be thrown.");
                        stillRun();
                        println();
                        break;
                    }
                    Point[] vectorPoints = new Point[trajectoryNodes.length * 2];

                    for (int i = 0; i < vectorPoints.length; i+=2) {
                        if (i == 0) {
                            vectorPoints[i] = new Point(trajectoryNodes[i].magnitude * Math.cos(Util.toRadians(trajectoryNodes[i].posTheta)) + trajectoryNodes[i].posX,
                                                        trajectoryNodes[i].magnitude * Math.sin(Util.toRadians(trajectoryNodes[i].posTheta)) + trajectoryNodes[i].posY);
                        } else {
                            vectorPoints[i] = new Point(trajectoryNodes[i/2].magnitude * Math.cos(Util.toRadians(trajectoryNodes[i/2].posTheta)) + trajectoryNodes[i/2].posX,
                                                        trajectoryNodes[i/2].magnitude * Math.sin(Util.toRadians(trajectoryNodes[i/2].posTheta)) + trajectoryNodes[i/2].posY);
                        }
                        vectorPoints[i + 1] = trajectory.pathNodes[i/2].toPoint();
                    }

                    TrajectoryPoint[] pointPoints = new TrajectoryPoint[trajectory.pathPoints.length];
                    for (int i = 0; i < trajectory.pathPoints.length; i++) {
                        pointPoints[i] = trajectory.pathPoints[i];
                    }
                    Util.toWindowGraph(pointPoints, vectorPoints);

                    println();
                    println("Preview sucessfully generated.");
                    println();
                    break;
                case "save":
                    trajectoryNodes = new TrajectoryNode[trajectoryNodeList.size()];
                    for (int i = 0; i < trajectoryNodes.length; i++) {
                        trajectoryNodes[i] = trajectoryNodeList.get(i);
                    } 

                    trajectory = new Trajectory(trajectoryNodes);
                    trajectory.generate();

                    JSONObject jsonPath = new JSONObject();
                    JSONObject jsonNodes = new JSONObject();

                    jsonPath.put("trajectory", trajectory.pathPoints);
                    jsonNodes.put("nodes", trajectory.pathNodes);

                    File pathFile = new File("out\\path.json");
                    File nodeFile = new File("out\\nodes.json");

                    FileWriter pathWriter = new FileWriter(pathFile);
                    FileWriter nodeWriter = new FileWriter(nodeFile);

                    pathWriter.write(jsonPath.toString());
                    nodeWriter.write(jsonNodes.toString());

                    pathWriter.close();
                    nodeWriter.close();

                    double fileSize = pathFile.length();
                    String sizeSuffix = "B";
                    if (fileSize > 1073741824) {
                        sizeSuffix = "GB";
                        fileSize /= 1073741824;
                    } else if (fileSize > 1048576) {
                        sizeSuffix = "MB";
                        fileSize /= 1048576;
                    } else if (fileSize > 1024) {
                        sizeSuffix = "KB";
                        fileSize /= 1024;
                    }

                    println();
                    println("Path sucessfully saved to out\\path.json.");
                    println("Current file size: " + (Math.round(fileSize * 100) * 1d/100) + " " + sizeSuffix);
                    println();
                    break;
                case "load":
                    command = input.split(" ");

                    if (command.length == 1) println("Please enter a path to the node file: ");
                    String path = command.length > 1 ? command[1] : keyboard.nextLine();
                    try {
                        File file = new File(path);
                        JSONObject jsonObject = new JSONObject(new Scanner(file).nextLine());

                        JSONArray nodeArray = jsonObject.getJSONArray("nodes");
                        trajectoryNodes = new TrajectoryNode[nodeArray.length()];
                        for (int i = 0; i < trajectoryNodes.length; i++) {
                            trajectoryNodes[i] = TrajectoryNode.parseNode((String) nodeArray.get(i));
                            if (trajectoryNodeList.size() > i) trajectoryNodeList.set(i, TrajectoryNode.parseNode((String) nodeArray.get(i)));
                            else trajectoryNodeList.add(TrajectoryNode.parseNode((String) nodeArray.get(i)));
                        }

                        println();
                        println("Nodes successfully pulled. (" + trajectoryNodes.length + ")");
                        println();
                        break;
                    } catch (FileNotFoundException e) {
                        println();
                        println("Could not found said file.");
                        stillRun();
                        println();
                        break;
                    }
                case "delete":
                    File file = new File("out\\path.json");
                    file.delete();
                    file = new File("out\\nodes.json");
                    
                    println();
                    println("File path.json sucessfully deleted.");
                    println();
                case "exit": 
                    loop = false;
                    break;
                default:
                    println();
                    println("Not a valid command.");
                    stillRun();
                    println();
            }
        }

        keyboard.close();
    }

    public static void println(String msg) {
        System.out.println(msg);
    }

    public static void stillRun() {
        println("Program is still running, please enter a new command:");
    }

    public static void println() {
        println("");
    }
}
