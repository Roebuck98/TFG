package src;

/**
 * Clase que almacena una Chain Instance. Consta de un Nodo y el tipo de SFC para cumplir una serie de cadena.
 * Cada uno de los objetos de esta clase se almacena en una cadena (por cada archivo) y, después, en un Mapa.
 */
public class CInstance {

    private Node n;
    private int SFC_type;

    /**
     * Constructor de la clase CInstance
     * @param n Nodo donde se aloja
     * @param SFC_type Tipo de la cadena SFC
     */
    public CInstance(Node n, int SFC_type){
        this.n = n;
        this.SFC_type = SFC_type;
    }

    /**
     * Retorna un entero con el tipo de SFC
     * @return el tipo de SFC
     */

    public int getSFC_type() {
        return SFC_type;
    }

    /**
     * Retorna el objeto Nodo donde se aloja
     * @return el Nodo donde se aloja
     */

    public Node getN() {
        return n;
    }
}
