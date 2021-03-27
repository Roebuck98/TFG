package src;

import java.util.*;

public class App {

    public static void main(String[] args) {
        
        InitData initdata = new InitData();

        Graph graph = new Graph();

        graph = initdata.init();

        graph.show();
        
        List<Node> nodes = new ArrayList<>();

        nodes.addAll(graph.getNodes());

        Node node = nodes.get(1);

        System.out.println("\n\nNombre del nodo: " + node.getName());

        graph = Dijkstra.calculateShortestPathFromSource(graph, node);
        
        graph.show();
        
        List<Node> sp = new ArrayList<>();

        sp = node.getShortestPath();

        System.out.println("Name + distance");

        for(Node n : sp){
            System.out.println(n.getName() + "       " + n.getDistance());
        }
    }
    
}