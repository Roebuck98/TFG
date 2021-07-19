package src;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import static java.lang.String.format;

/**
 * Clase para manejar los caminos. Cada camino está compuesto de nodos, enlaces, su coste total y el MLU.
 * Se compone de eslabones de su subclase Non-Empty Path que apuntan siempre al predecesor, obteniendo así el camino inverso.
 */
public class Path {

    private final Node node;
    private final double totalCost;
    private final double load;
    private final double MAX_LOAD;
    private static double MLU;

    /**
     * Constructor parametrizado que sólo necesita el nodo origen
     * @param source El nodo origen
     */
    public Path(Node source) {
        Objects.requireNonNull(source, "The input source node is null.");
        node = source;
        totalCost = 0.0;
        load = 0.0;
        MAX_LOAD = 0.0;
        MLU = 0.0;
    }

    /**
     * Constructor parametrizado
     * @param node Nodo origen del camino
     * @param totalCost El coste total del camino
     * @param load La suma de las cargas totales del camino
     * @param max_load La suma de las cargas máximas de los enlaces del camino
     * @param MLU La carga máxima de un enlace, simbolizando si el camino entero está disponible o no
     */
    private Path(Node node, double totalCost, double load, double max_load, double MLU) {
        this.node = node;
        this.totalCost = totalCost;
        this.load = load;
        this.MAX_LOAD = max_load;
        this.MLU = MLU;
    }

    /**
     * Comprueba su un enlace extiende o no el camino. Si no lo extiende, se producirá un error.
     * @param edge El enlace a comprobar
     * @return El camino con el nuevo enlace y nodo
     */
    public Path append(Link edge) {
        if (!node.equals(edge.getA())) {
            throw new IllegalArgumentException(format("The edge %s doesn't extend the path %s", edge, this.getNodeList()));
        }

        return new NonEmptyPath(this, edge);
    }

    /**
     * Devuelve el nodo final del camino
     * @return El nodo final del camino
     */
    public Node getEndNode() {
        return node;
    }

    /**
     * Devuelve la lista de nodos de la que se compone el camino. Ordenada de acuerdo a los nodos por los que pasa.
     * @return La Lista de Nodos total
     */
    public List<Node> getNodeList() {
        return new ArrayList<>();
    }

    /**
     * Devuelve el coste total del camino
     * @return Un Doble con el coste del camino
     */
    public double pathCost() {
        return totalCost;
    }

    /**
     * Devuelve la carga del camino. No debe superar nunca el máximo.
     * @return Un doble con la carga
     */
    public double getLoad(){
        return load;
    }

    /**
     * Devuelve la carga máxima del camino, que resulta ser la suma de todas lñas cargas máximas de los enlaces.
     * @return Un doble con la carga máxima
     */
    public double getMAX_LOAD() {
        return MAX_LOAD;
    }

    /**
     * Devuelve el porcentaje de máxima ocupación del enlace más ocupado.
     * @return Un doble con el porcentaje más alto del camino.
     */
    public double getMLU() {
        return MLU;
    }

    /**
     * Establece cual es el prcentaje de ocupación más alto entre los enlaces del camino.
     * @param mlu Un doble con el porcentaje más alto
     */
    public void setMLU(double mlu){
        MLU = mlu;
    }

    /**
     * Clase que indica que en el camino, existe una secuencia de nodos en vez de sólo el nodo origen
     */
    private static class NonEmptyPath extends Path {
        private final Path predecessor;

        /**
         * Constructir parametriizado
         * @param path El camino recorrido hasta ahora, donde se le añadirá el nuevo enlace.
         * @param edge El enlace a añadir
         */
        public NonEmptyPath(Path path, Link edge) {
            super(edge.getB(), path.totalCost + (double) edge.getCost(),
                    path.load + edge.getBandwidth(), path.MAX_LOAD + edge.MAX_BAND, MLU);

            MLU = path.getMLU();
            if(MLU < ((edge.getBandwidth()/ edge.MAX_BAND)*100)){
                MLU = (edge.getBandwidth()/ edge.MAX_BAND)*100;
                path.setMLU(MLU);
            }
            predecessor = path;

        }

        /**
         * Devuelve la lista completa de los nodos del camino, en orden secuencial de recorrido
         * @return Una Lista de Nodos
         */
        @Override
        public List<Node> getNodeList() {
            LinkedList<Node> result = new LinkedList<>();
            Path path = this;
            while(path instanceof NonEmptyPath) {
                result.addFirst(path.node);
                path = ((NonEmptyPath) path).predecessor;
            }
            result.addFirst(path.node);
            return result;
        }
    }
}
