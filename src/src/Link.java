package src;

public class Link {

    private int ID;
    private Node a;
    private Node b;
    private int cost;
    protected double MAX_BAND;
    private double bandwidth;


    public Link(int ID, Node a, Node b, double MAX_BAND) {
        this.ID = ID;
        this.a = a;
        this.b = b;
        this.cost = 1;
        this.MAX_BAND = MAX_BAND;
        bandwidth = 0.0;
    }

    public Node getA() {
        return a;
    }

    public Node getB() {
        return b;
    }

    public int getCost() {
        return cost;
    }

    public double getBandwidth() {
        return bandwidth;
    }

    public void addBW(double bw){
        bandwidth = bandwidth + bw;
    }

    public void subBW(double bw){
        bandwidth = bandwidth - bw;
    }


}
