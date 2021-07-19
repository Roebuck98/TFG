package src;

import java.util.*;

/**
 * Clase utilizada para encontrar una lista de caminos más cortos según su coste. Si se quiere sólo encontrar el camino más corto,
 *  se debe invocar con el atributo K = 0.
 */
public class DefaultKShortestPathFinder {

    /**
     * Encuentra una lista de caminos más cortos según el coste de los enlaces.
     * @param source Nodo inicial, del que parte el camino
     * @param target Nodo final, a donde se dirige el camino
     * @param graph El grafo indicando todos los caminos posibles
     * @param k El número de caminos a encontrar
     * @return
     */
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

    /**
     * Comprueba que K nunca sea 0, o si no, no encontraría caminos
     * @param k El número de caminos a encontrar
     */
    void checkK(int k) {
        if (k < 1) {
            throw new IllegalArgumentException(
                    String.format("The value of k is too small: %d, should be at least 1.", k));
        }
    }
}
