package src;

/**
 * Clase enlace (Link). Indica características necesarias en un enlace de un grafo. Pensado para grafos unidireccionales.
 */
public class Link {

    private int ID;
    private Node a;
    private Node b;
    private int cost;
    protected double MAX_BAND;
    private double bandwidth;

    /**
     * Constructor de la clase Link
     * @param ID Identificador del Link
     * @param a Nodo del que parte
     * @param b Nodo al que se dirige
     * @param MAX_BAND Máximo de capacidad del Link
     */

    public Link(int ID, Node a, Node b, double MAX_BAND) {
        this.ID = ID;
        this.a = a;
        this.b = b;
        this.cost = 1;
        this.MAX_BAND = MAX_BAND;
        bandwidth = 0.0;
    }

    /**
     * Retorna el objeto Nodo del que parte
     * @return el Nodo del que parte
     */
    public Node getA() {
        return a;
    }

    /**
     * Retorna el objeto Nodo al que se dirige
     * @return el Nodo al que se dirige
     */
    public Node getB() {
        return b;
    }

    /**
     * Retorna el coste del Link, usado para calcular el peso del Path
     * @return un Entero indicador del coste del Link
     */
    public int getCost() {
        return cost;
    }

    /**
     * Retorna la ocupación del ancho de banda actual del Link
     * @return un Doble con la ocupación del Link
     */
    public double getBandwidth() {
        return bandwidth;
    }

    /**
     * Aumenta la ocupación del ancho de banda
     * @param bw la cantidad añadida al ancho de banda actual
     */
    public void addBW(double bw){
        bandwidth = getBandwidth() + bw;

        if (bandwidth > MAX_BAND){
            System.out.println("VALOR NEGATIVO, COMPROBAR ERROR: " + bandwidth+ " %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        }
    }

    /**
     * Disminuye la ocupación del ancho de banda dado una cantidad
     * @param bw la cantidad que se sustrae del ancho de banda ocupado
     */
    public void subBW(double bw){
        bandwidth = getBandwidth() - bw;

        if (bandwidth < 0.0){
            System.out.println("VALOR NEGATIVO, COMPROBAR ERROR: " + bandwidth+ " %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        }
    }


}
