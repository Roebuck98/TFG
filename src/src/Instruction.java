package src;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase POJO par manejar la información de una solicitud SFC
 */
public class Instruction {

    private int ID;

    private Node IN;
    private Node EN;

    private int SFC;

    private double TCAM;

    private Node actual;
    private Node installNode;
    private int SFCactual;

    private List<Node> path;

    private List<CInstance> ci;

    private List<Integer> slots = new ArrayList<>();

    private boolean installed;
    private boolean satisfied;
    private int inactive;
    protected int MAX_INACTIVE;

    /**
     * Constructor parametrizado de una Instrucción
     * @param ID Identificador de la instrucción
     * @param IN Nodo de ingreso
     * @param EN Nodo de salida
     * @param TCAM Espacio de memoria a ocupar
     * @param SFC Tipo de SFC que tiene que cumplir su cadena
     */
    public Instruction(int ID, Node IN, Node EN, double TCAM, int SFC){
        this.ID = ID;
        this.IN = IN;
        this.EN = EN;
        this.SFC = SFC;
        this.TCAM = TCAM;
        SFCactual = 0;
        path = new ArrayList<>();
        ci = new ArrayList<>();
        inactive = 0;
        installed = false;
        satisfied = false;
    }

    /**
     * Retorna el tipo de SFC
     * @return Un Entero con el tipo de SFC
     */
    public int getSFC() {
        return SFC;
    }

    /**
     * Retorna el nodo de salida, Egress Node
     * @return El Nodo de salida
     */
    public Node getEN() {return EN;}

    /**
     * Retorna el nodo de entrada, Ingress Node
     * @return El nodo de entrada
     */
    public Node getIN() {return IN;}

    /**
     * Establece los slots de tiempo activo de una instrucción
     * @param slots La Lista de Enteros indicadores de slots de tiempo
     */
    public void setSlots(List<Integer> slots) {
        this.slots = slots;
    }

    /**
     * Devuelve el identificador de la solicitud
     * @return El identificador de la instrucción
     */
    public int getID() {
        return ID;
    }

    /**
     * Retorna el nodo donde, actualmente, se encuentra la instrucción (NO es lo mismo que donde estáinstalado)
     * @return El nodo actual
     */
    public Node getActual() {
        return actual;
    }

    /**
     * Establece donde se encuentra ahora la instrucción
     * @param actual El nodo actual
     */
    public void setActual(Node actual) {
        this.actual = actual;
    }

    /**
     * Establece el SFC ya cumplido, indicando cual es el siguiente
     * @param SFCactual Un entero con el SFC cumplido
     */
    public void setSFCactual(int SFCactual) {
        this.SFCactual = SFCactual;
        //MAX_INACTIVE = MAX_INACTIVE + 1;
    }

    /**
     * Retorna el valor del SFC que está cumplido hasta ahora
     * @return Un Entero con el SFC cumlpido hasta ahora
     */
    public int getSFCactual() {
        return SFCactual;
    }

    /**
     * Devuelve el valor de la TCAM
     * @return El valor de la TCAM
     */
    public double getTCAM() {
        return TCAM;
    }

    /**
     * Establece la lista de cadenas para una instrucción
     * @param ci La lista de cadenas para una instrucción
     */
    public void setCi(List<CInstance> ci) {
        this.ci = ci;
    }

    /**
     * Retorna la lista de cadenas asociadas a una instrucción
     * @return La Lista de instancias de cadenas
     */
    public List<CInstance> getCi() {
        return ci;
    }

    /**
     * Retorna los slots de tiempo asociados a una instrucción
     * @return La lista de slots de tiempo
     */
    public List<Integer> getSlots() {
        return slots;
    }

    /**
     * Retorna una cantidad de tiempo inactivo
     * @return El número de slots inactivos seguidos
     */
    public int getInactive() {
        return inactive;
    }

    /**
     * Retorna la cantidad máxima de tiempo que la instrucción puede estar inactiva
     * @return Un Entero que indica la cantidad de slots de tiempo máximo inactivo que puede estar una solicitud
     */
    public int getMAX_INACTIVE() {
        return MAX_INACTIVE;
    }

    /**
     * Establece el tiempo máximo de inactividad de una solicitud
     * @param MAX_INACTIVE Un entero indicando el tiempo máximo
     */
    public void setMAX_INACTIVE(int MAX_INACTIVE) {
        this.MAX_INACTIVE = MAX_INACTIVE;
    }

    /**
     * Establece el tiempo de inactividad actual
     * @param inactive La cantidad de tiempo inactivo actual
     */
    public void setInactive(int inactive) {
        this.inactive = inactive;
    }

    /**
     * Retorna si la instrucción está instalada en algún nodo
     * @return True si está instalada
     */
    public boolean isInstalled() {
        return installed;
    }

    /**
     * Establece si la instrucción está instalada
     * @param installed si la instrucción está instalada
     */
    public void setInstalled(boolean installed) {
        this.installed = installed;
    }

    /**
     * Establece si una instrucción se ha satisfecho
     * @param satisfied El valor indicador si la instrucción se ha satisfecho
     */
    public void setSatisfied(boolean satisfied) {
        this.satisfied = satisfied;
    }

    /**
     * Retorna una lista de nodos indicando el recorrido que ha hecho la instrucción
     * @return Una Lista de Nodos recorridos por la instrucción
     */
    public List<Node> getPath() {
        return path;
    }

    /**
     * Añade un nodo al camino recorrido por la instrucción
     * @param n El nodo a añadir
     */
    public void addNodeToPath(Node n){
        path.add(n);
    }

    /**
     * Retorna si una instrucción se ha satisfecho o no
     * @return True si se ha satisfecho o no
     */
    public boolean isSatisfied() {
        return satisfied;
    }

    /**
     * Establece el nodo Clasificador, es decir, donde se ha instalado la solicitud
     * @param installNode El Nodo donde se ha instalado
     */
    public void setInstallNode(Node installNode) {
        this.installNode = installNode;
    }

    /**
     * Retorna el nodo Clasificador, donde se haya instalado la instrucción
     * @return El nodo donde se ha instalado
     */
    public Node getInstallNode() {
        return installNode;
    }
}
