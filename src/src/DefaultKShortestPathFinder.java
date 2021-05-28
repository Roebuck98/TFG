package src;

import java.util.*;

public class DefaultKShortestPathFinder {

    public List<Path> findShortestPaths(Node source, Node target, Graph graph, int k) {

        checkK(k);

        List<Path> paths = new ArrayList<>(k);
        Map<Node, Integer> countMap = new HashMap<>();
        Queue<Path> HEAP = new PriorityQueue<>(
                Comparator.comparingDouble(Path::pathCost));

        HEAP.add(new Path(source));

        while (!HEAP.isEmpty() && countMap.getOrDefault(target, 0) < k) {
            Path currentPath = HEAP.remove();
            Node endNode = currentPath.getEndNode();

            countMap.put(endNode, countMap.getOrDefault(endNode, 0) + 1);

            if (endNode.equals(target)) {
                paths.add(currentPath);
            }

            if (countMap.get(endNode) <= k) {
                for (Link edge : graph.getLinksFrom(endNode)) {
                    Path path = currentPath.append(edge);
                    HEAP.add(path);
                }
            }
        }

        return paths;
    }

    void checkK(int k) {
        if (k < 1) {
            throw new IllegalArgumentException(
                    String.format("The value of k is too small: %d, should be at least 1.", k));
        }
    }
}
