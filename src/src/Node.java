package src;

import java.sql.Array;
import java.util.*;

public class Node {

    private String name;
    private int ID;
    private ArrayList<Instruction> TCAM;
    private List<Node> shortestPath = new LinkedList<>();
    private List<Graph> kShortestPaths = new LinkedList<>();
    protected final int MEMORY_SIZE_TOTAL = 5;
    private List <Instruction> buffer = new LinkedList<>();
    private List <VNF> VNFs = new LinkedList<>();

    Map<Node, Link> adjacentNodes = new HashMap<>();

    public Node(String name, int ID){
        this.name = name;
        this.ID = ID;
        this.TCAM = new ArrayList<>();
    }

    public Node(){
        this.name = "";
        this.ID = -1;
        this.TCAM = new ArrayList<>();
    }

    public ArrayList<Instruction> getTCAM() {
        return TCAM;
    }

    public void setTCAM(ArrayList<Instruction> TCAM) {
        this.TCAM = TCAM;
    }

    public void addInstruction(Instruction Ins){
        TCAM.add(Ins);
    }

    public void addVNF(VNF v){ VNFs.add(v);}

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

    public List<VNF> getVNFs() {
        return VNFs;
    }

    public void addDestination(Node destination, Link link) {
        adjacentNodes.put(destination, link);
    }
    public void setAdjacentNodes(Map<Node, Link> adjacentNodes) {
        this.adjacentNodes = adjacentNodes;
    }

    public Map<Node, Link> getAdjacentNodes() {
        return adjacentNodes;
    }
}
