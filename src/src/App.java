package src;

import java.util.*;

public class App {

    public static void main(String[] args) {
        
        InitData initdata = new InitData();

        Graph graph;

        graph = initdata.init();

        List<Node> nodes = new ArrayList<>();

        nodes.addAll(graph.getNodes());

        Node init = graph.searchByID(0);

        Node end = graph.searchByID(4);

        List<Path> paths = new DefaultKShortestPathFinder()
                .findShortestPaths(init, end, graph, 5);

        for (Path path : paths) {
            for (Node n:
                 path.getNodeList()) {
                System.out.print(n.getName() + ", ");
            }


            System.out.println(" " + path.pathCost());
        }



    }
    
}