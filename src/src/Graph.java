package src;
/**
 * Clase Grafo. Indica el mapa topográfico total de la red mediante un grafo.
 * Los nodos son los vértices del grafo.
 * Los enlaces son las aristas del grafo.
 */

import java.util.*;

public class Graph {
    private List<Node> nodes = new ArrayList<>();
    private List<Link> links = new ArrayList<>();

    /**
     * Retorna la lista con todos los nodos
     * @return Un ArrayList con todos los nodos del grafo
     */
    public List<Node> getNodes() {
        return nodes;
    }

    /**
     * Establece una lista de nodos en la red
     * @param nodes Una Lista con todos los nodos
     */
    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }

    /**
     * Establece una lista con los enlaces de la red
     * @param links La Lista con los enlaces
     */
    public void setLinks(List<Link> links) {
        this.links = links;
    }

    /**
     * Busca un nodo dado un ID identificativo
     * @param ID Un Entero con el ID del nodo
     * @return Un objeto Nodo
     */
    public Node searchByID(int ID){

        Node searchedNode = null;

        for (Node node: nodes) {
            if(node.getID() == ID){
                searchedNode = node;
            }
        }

        return searchedNode;
    }

    /**
     * Busca un enlace dado dos nodos y lo devuelve
     * @param a El nodo inicial del enlace
     * @param b El nodo al que se dirige el enlace
     * @return El objeto enlace (Link)
     */
    public Link searchLink(Node a, Node b){
        Link searchedLink = null;

        for (Link link:
             links) {
            if(link.getA().equals(a) && link.getB().equals(b) ){
                searchedLink = link;
            }
        }

        return searchedLink;
    }

    /**
     * Obtiene una lista de enlaces dado un nodo. Dichos enlaces parten de ese nodo.
     * @param from El nodo del que parten todos los enlaces
     * @return Una Colección con todos los enlaces.
     */
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

    /**
     * Comprueba si un camino está disponible (si su ancho de banda no está completamente ocupado,
     * es decir, MLU == 100) si se le añade una cierta cantidad de ancho de banda nuevo.
     * @param nodes La lista de nodos que conforman el camino
     * @param amount La cantidad de ancho de banda que se añadiría
     * @return True si el camino al añadir el nuevo ancho de banda está disponible
     */
    public boolean checkAvailablePath(List<Node> nodes, double amount){
        for (int i = 0; i < nodes.size()-1; i++){
            Link l = searchLink(nodes.get(i), nodes.get(i+1));
            if (l.getBandwidth() + amount > l.MAX_BAND){
                System.out.println("Camino ocupado");
                return false;

            }
        }

        return true;
    }

}
