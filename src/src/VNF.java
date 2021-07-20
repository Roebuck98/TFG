package src;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa los VNF.
 */

public class VNF {
    Node n;
    int SFCtype;
    double bandwidth;
    double actualbd;
    List <Instruction> SFC;

    /**
     * Constructor. Asigna el valor 0 al ancho de banda actual en su creación, dado que indica la ocupación del mismo.
     * @param n Nodo donde se va a alojar
     * @param SFCtype Tipo de SFC que cumple
     * @param bandwidth Ancho de banda máximo
     */
    public VNF(Node n, int SFCtype, double bandwidth){
        this.n = n;
        this.SFCtype = SFCtype;
        this.bandwidth = bandwidth;
        SFC = new ArrayList<>();
        actualbd = 0.0;
    }

    /**
     * Devuelve el tipo de SFC
     * @return Un entero con el tipo de SFC
     */
    public int getSFCtype() {
        return SFCtype;
    }

    /**
     * Añade una instrucción a la lista para poder manejarla.
     * @param ins La Instrucción a añadir
     */
    public void addSFC(Instruction ins){
        SFC.add(ins);
    }

    /**
     * Actualiza el valor del ancho de banda actual.
     * @param actualbd El valor ya actualizado.
     */
    public void setActualbd(double actualbd) {
        this.actualbd = actualbd;
    }

    /**
     * Devuelve el valor actual del ancho de banda
     * @return El valor actual del ancho de banda
     */
    public double getActualbd() {
        return actualbd;
    }

    /**
     * Elimina la instrucción de la lista.
     * @param ins La instrucción a eliminar
     */
    public void removeSFC(Instruction ins){
        SFC.remove(ins);
    }

    /**
     * Devuelve el máximo de capacidad de un VNF
     * @return La capacidad máxima de un VNF
     */
    public double getBandwidth() {
        return bandwidth;
    }
}
