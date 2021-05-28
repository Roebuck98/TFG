package src;

import java.util.*;

public class Node {

    private String name;
    private int ID;
    private List<Node> shortestPath = new LinkedList<>();
    private List<Graph> kShortestPaths = new LinkedList<>();
    private int MEMORY_SIZE_TOTAL;
    private List <Instruction> buffer = new LinkedList<>();

    Map<Node, Link> adjacentNodes = new HashMap<>();

    public void addDestination(Node destination, Link link) {
        adjacentNodes.put(destination, link);
    }
    public void setAdjacentNodes(Map<Node, Link> adjacentNodes) {
        this.adjacentNodes = adjacentNodes;
    }

    public Map<Node, Link> getAdjacentNodes() {
        return adjacentNodes;
    }


    public Node(String name, int ID){this.name = name; this.ID = ID;}

    public String getName() {
        return name;
    }

    public List<Node> getShortestPath() {
        return shortestPath;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setShortestPath(List<Node> shortestPath) {
        this.shortestPath = shortestPath;
    }

    public void setkShortestPaths(List<Graph> ksp){
        this.kShortestPaths = ksp;
    }

    public List<Graph> getkShortestPaths() {
        return kShortestPaths;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
}
