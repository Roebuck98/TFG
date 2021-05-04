package src;

public class Link {

    private Node a;
    private Node b;
    private int cost;

    public Link(Node a, Node b, int cost) {

        this.a = a;
        this.b = b;
        this.cost = cost;
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
}
