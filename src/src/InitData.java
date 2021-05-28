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
    Link linkA2;
    Link linkB2;
    Link linkC2;
    Link linkD2;
    Link linkE2;
    Link linkF2;
    Link linkG2;
    Link linkH2;
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
        linkA2 = new Link(nodeB, nodeA, 10);

        linkB = new Link(nodeA, nodeC, 15);
        linkB2 = new Link(nodeC, nodeA, 15);

        linkC = new Link(nodeB, nodeD, 12);
        linkC2 = new Link(nodeD, nodeB, 12);

        linkD = new Link(nodeB, nodeF, 15);
        linkD2 = new Link(nodeF, nodeB, 15);

        linkE = new Link(nodeE, nodeC, 10);
        linkE2 = new Link(nodeC, nodeE, 10);

        linkF = new Link(nodeD, nodeE, 2);
        linkF2 = new Link(nodeE, nodeD, 2);

        linkG = new Link(nodeF, nodeD, 1);
        linkG2 = new Link(nodeD, nodeF, 1);

        linkH = new Link(nodeE, nodeF, 5);
        linkH2 = new Link(nodeF, nodeE, 5);


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
        graph.addLink(linkA2);
        graph.addLink(linkB2);
        graph.addLink(linkC2);
        graph.addLink(linkD2);
        graph.addLink(linkE2);
        graph.addLink(linkF2);
        graph.addLink(linkG2);
        graph.addLink(linkH2);

        return graph;
    }
}
