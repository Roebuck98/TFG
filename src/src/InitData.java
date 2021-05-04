package src;

public class InitData {

    Node nodeA;
    Node nodeB;
    Node nodeC;
    Node nodeD;
    Node nodeE;
    Node nodeF;
    Link linkA;
    Link linkB;
    Link linkC;
    Link linkD;
    Link linkE;
    Link linkF;
    Link linkG;
    Link linkH;
    Graph graph;

    public InitData() {
        nodeA = new Node("A", 0);
        nodeB = new Node("B", 1);
        nodeC = new Node("C", 2);
        nodeD = new Node("D", 3);
        nodeE = new Node("E", 4);
        nodeF = new Node("F", 5);
        graph = new Graph();


        linkA = new Link(nodeA, nodeB, 10);
        linkB = new Link(nodeA, nodeC, 15);

        linkC = new Link(nodeB, nodeD, 12);
        linkD = new Link(nodeB, nodeF, 15);

        linkE = new Link(nodeE, nodeC, 10);

        linkF = new Link(nodeD, nodeE, 2);
        linkG = new Link(nodeF, nodeD, 1);

        linkH = new Link(nodeE, nodeF, 5);

    }

    public Graph init() {

        nodeA.addDestination(nodeB, linkA);
        nodeA.addDestination(nodeC, linkB);
        nodeB.addDestination(nodeA, linkA);
        nodeC.addDestination(nodeA, linkB);

        nodeB.addDestination(nodeD, linkC);
        nodeB.addDestination(nodeF, linkD);
        nodeD.addDestination(nodeB, linkC);
        nodeF.addDestination(nodeB, linkD);

        nodeC.addDestination(nodeE, linkE);
        nodeE.addDestination(nodeC, linkE);

        nodeD.addDestination(nodeE, linkF);
        nodeD.addDestination(nodeF, linkG);
        nodeE.addDestination(nodeD, linkF);
        nodeF.addDestination(nodeD, linkG);

        nodeF.addDestination(nodeE, linkH);
        nodeE.addDestination(nodeF, linkH);

        graph.addNode(nodeA);
        graph.addNode(nodeB);
        graph.addNode(nodeC);
        graph.addNode(nodeD);
        graph.addNode(nodeE);
        graph.addNode(nodeF);

        graph.addLink(linkA);
        graph.addLink(linkB);
        graph.addLink(linkC);
        graph.addLink(linkD);
        graph.addLink(linkE);
        graph.addLink(linkF);
        graph.addLink(linkG);
        graph.addLink(linkH);

        return graph;
    }
}
