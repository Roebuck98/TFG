package src;

import java.util.*;
import java.util.Map.Entry;

public class Dijkstra {

    private static int[] pathValues;

    public static Graph calculateShortestPathFromSource(Graph graph, Node source) {
        pathValues = new int[graph.getNodes().size()];

        pathValues[source.getID()] = 0;

        for (int i= 0; i < graph.getNodes().size(); i++) {
            if(i != source.getID())
                pathValues[i] = Integer.MAX_VALUE;

        }

        Set<Node> settledNodes = new HashSet<>();

        Set<Node> unsettledNodes = new HashSet<>();

        unsettledNodes.add(source);

        System.out.println("Nodo de entrada: " + source.getName());

        while (unsettledNodes.size() != 0) {
            Node currentNode = getLowestDistanceNode(unsettledNodes);
            unsettledNodes.remove(currentNode);
            for (Entry <Node, Link> adjacencyPair:
                    currentNode.getAdjacentNodes().entrySet()) {
                Node adjacentNode = adjacencyPair.getKey();
                Integer edgeWeight = adjacencyPair.getValue().getCost();
                if (!settledNodes.contains(adjacentNode)) {
                    calculateMinimumDistance(adjacentNode, edgeWeight, currentNode);
                    unsettledNodes.add(adjacentNode);
                }
            }
            settledNodes.add(currentNode);
        }
        for (int i= 0; i < graph.getNodes().size(); i++) {
            System.out.println("Valor camino más corto a : " + graph.searchByID(i).getName() + " -> " + pathValues[i]);
        }

        return graph;
    }

    /**
     * The getLowestDistanceNode() method, returns the node with the lowest distance
     * from the unsettled nodes set,
     *
     * @param unsettledNodes
     * @return
     */
    private static Node getLowestDistanceNode(Set<Node> unsettledNodes) {
        Node lowestDistanceNode = null;
        int lowestDistance = Integer.MAX_VALUE;
        for (Node node : unsettledNodes) {
            int nodeDistance = pathValues[node.getID()];
            if (nodeDistance < lowestDistance) {
                lowestDistance = nodeDistance;
                lowestDistanceNode = node;
            }
        }
        return lowestDistanceNode;
    }


    /**
     * while the calculateMinimumDistance() method compares the actual distance
     * with the newly calculated one while following the newly explored path
     * @param evaluationNode
     * @param edgeWeigh
     * @param sourceNode
     */
    private static void calculateMinimumDistance(Node evaluationNode, Integer edgeWeigh, Node sourceNode) {
        Integer sourceDistance = pathValues[sourceNode.getID()];
        if (sourceDistance + edgeWeigh < pathValues[evaluationNode.getID()]) {
            pathValues[evaluationNode.getID()] = sourceDistance + edgeWeigh;
            LinkedList<Node> shortestPath = new LinkedList<>(sourceNode.getShortestPath());
            shortestPath.add(sourceNode);
            evaluationNode.setShortestPath(shortestPath);
        }
    }
}
