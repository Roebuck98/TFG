package src;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import static java.lang.String.format;

public class Path {

    private final Node node;
    private final double totalCost;

    public Path(Node source) {
        Objects.requireNonNull(source, "The input source node is null.");
        node = source;
        totalCost = 0.0;
    }

    private Path(Node node, double totalCost) {
        this.node = node;
        this.totalCost = totalCost;
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

    private static class NonEmptyPath extends Path {
        private final Path predecessor;

        public NonEmptyPath(Path path, Link edge) {
            super(edge.getB(), path.totalCost + (double) edge.getCost());
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
