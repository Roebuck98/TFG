package src;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import static java.lang.String.format;

public class Path {

    private final Node node;
    private final double totalCost;
    private final double load;
    private final double MAX_LOAD;
    private static double MLU;

    public Path(Node source) {
        Objects.requireNonNull(source, "The input source node is null.");
        node = source;
        totalCost = 0.0;
        load = 0.0;
        MAX_LOAD = 0.0;
        MLU = 0.0;
    }

    private Path(Node node, double totalCost, double load, double max_load, double MLU) {
        this.node = node;
        this.totalCost = totalCost;
        this.load = load;
        this.MAX_LOAD = max_load;
        this.MLU = MLU;
    }


    public Path append(Link edge) {
        if (!node.equals(edge.getA())) {
            throw new IllegalArgumentException(format("The edge %s doesn't extend the path %s", edge, this.getNodeList()));
        }

        return new NonEmptyPath(this, edge);
    }

    public Node getEndNode() {
        return node;
    }


    public List<Node> getNodeList() {
        return new ArrayList<>();
    }

    public double pathCost() {
        return totalCost;
    }

    public double getLoad(){
        return load;
    }

    public double getMAX_LOAD() {
        return MAX_LOAD;
    }

    public double getMLU() {
        return MLU;
    }

    public void setMLU(double mlu){
        MLU = mlu;
    }

    private static class NonEmptyPath extends Path {
        private final Path predecessor;

        public NonEmptyPath(Path path, Link edge) {
            super(edge.getB(), path.totalCost + (double) edge.getCost(),
                    path.load + (double) edge.getBandwidth(), path.MAX_LOAD + (double)edge.MAX_BAND, MLU);

            MLU = path.getMLU();
            if(MLU < ((edge.getBandwidth()/(double)edge.MAX_BAND)*100)){
                MLU = (edge.getBandwidth()/(double)edge.MAX_BAND)*100;
                path.setMLU(MLU);
            }
            predecessor = path;

        }

        @Override
        public List<Node> getNodeList() {
            LinkedList<Node> result = new LinkedList<>();
            Path path = this;
            while(path instanceof NonEmptyPath) {
                result.addFirst(path.node);
                path = ((NonEmptyPath) path).predecessor;
            }
            result.addFirst(path.node);
            return result;
        }
    }
}
