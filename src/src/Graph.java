package src;

import java.util.*;

public class Graph {
    private List<Node> nodes = new ArrayList<>();
    private List<Link> links = new ArrayList<>();

    public void addNode(Node nodeA) {
        nodes.add(nodeA);
    }

    public void addLink(Link link) {
        links.add(link);
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public Node searchByID(int ID){

        Node searchedNode = null;

        for (Node node: nodes) {
            if(node.getID() == ID){
                searchedNode = node;
            }
        }

        return searchedNode;
    }

    public Link searchLink(Node a, Node b){
        Link searchedLink = null;

        for (Link link:
             links) {
            if(link.getA().equals(a) && link.getB().equals(b) || link.getA().equals(b) && link.getB().equals(a) ){
                searchedLink = link;
            }
        }

        return searchedLink;
    }

    public Collection<Link> getLinksFrom(Node from) {
        Collection <Link> searchedLink = new ArrayList<>();

        for (Link link:
                links) {
            if(link.getA().equals(from)){
                searchedLink.add(link);
            }
        }

        return searchedLink;
    }

    public List<Node> getNodesBySFC(int i){
        List<Node> nds = new ArrayList<>();
        for (Node n: nodes) {
            for (VNF v: n.getVNFs()) {
                if(v.getSFCtype() == i && !nds.contains(n)){
                    nds.add(n);
                }
            }
        }


        return nds;
    }

}
