package src;

import java.util.*;

public class Graph {
    private Set<Node> nodes = new HashSet<>();
    private Set<Link> links = new HashSet<>();

    public void addNode(Node nodeA) {
        nodes.add(nodeA);
    }

    public void addLink(Link link) {
        links.add(link);
    }

    public Set<Node> getNodes() {
        return nodes;
    }

    public Set<Link> getLinks() {
        return links;
    }

    public void setNodes(Set<Node> nodes) {
        this.nodes = nodes;
    }

    public void setLinks(Set<Link> links) {
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

}
