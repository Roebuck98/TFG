package src;

public class CInstance {

    private Node n;
    private int SFC_type;

    public CInstance(Node n, int SFC_type){
        this.n = n;
        this.SFC_type = SFC_type;
    }

    public int getSFC_type() {
        return SFC_type;
    }

    public Node getN() {
        return n;
    }
}
