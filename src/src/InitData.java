package src;

public class InitData {

    Node nodeA ;
    Node nodeB ;
    Node nodeC ;
    Node nodeD ; 
    Node nodeE ;
    Node nodeF ;
    Graph graph;

    public InitData(){
        nodeA = new Node("A");
        nodeB = new Node("B");
        nodeC = new Node("C");
        nodeD = new Node("D"); 
        nodeE = new Node("E");
        nodeF = new Node("F");
        graph = new Graph();
    }

    public Graph init(){
       

        nodeA.addDestination(nodeB, 10);
        nodeB.addDestination(nodeA, 10);
        nodeA.addDestination(nodeC, 15);
        nodeC.addDestination(nodeA, 15);

        nodeB.addDestination(nodeD, 12);
        nodeB.addDestination(nodeF, 15);
        nodeD.addDestination(nodeB, 12);
        nodeF.addDestination(nodeB, 15);

        nodeC.addDestination(nodeE, 10);
        nodeE.addDestination(nodeC, 10);

        nodeD.addDestination(nodeE, 2);
        nodeD.addDestination(nodeF, 1);
        nodeE.addDestination(nodeD, 2);
        nodeF.addDestination(nodeD, 1);

        nodeF.addDestination(nodeE, 5);
        nodeE.addDestination(nodeF, 5);

        

        graph.addNode(nodeA);
        graph.addNode(nodeB);
        graph.addNode(nodeC);
        graph.addNode(nodeD);
        graph.addNode(nodeE);
        graph.addNode(nodeF);

        return graph;
    }
}
