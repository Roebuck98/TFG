package src;

public class Link {

    private int ID;
    private Node a;
    private Node b;
    private int cost;
    protected final int MAX_BAND = 10;
    private int bandwidth;


    public Link(int ID, Node a, Node b, int cost) {
        this.ID = ID;
        this.a = a;
        this.b = b;
        this.cost = cost;
        bandwidth = 0;
    }

    public Node getA() {
        return a;
    }

    public Node getB() {
        return b;
    }


    public void setA(Node a) {
        this.a = a;
    }

    public void setB(Node b) {
        this.b = b;
    }


    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getBandwidth() {
        return bandwidth;
    }

    public void setBandwidth(int bandwidth) {
        if(this.bandwidth + bandwidth > MAX_BAND){
            System.out.println("Cannot take this link because it would cause overflow");
        }else{
            this.bandwidth = bandwidth;
        }


    }


}
