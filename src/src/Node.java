package src;

import java.util.*;

/**
 * Clase POJO que maneja la información de un Nodo.
 */
public class Node {

    private String name;
    private int ID;
    private ArrayList<Instruction> TCAM;
    protected int MEMORY_SIZE_TOTAL;
    private List <Instruction> buffer = new LinkedList<>();
    private List <VNF> VNFs = new LinkedList<>();

    /**
     * Constructor parametrizado
     * @param name Nombre del nodo
     * @param ID Identificador del nodo
     */
    public Node(String name, int ID){
        this.name = name;
        this.ID = ID;
        this.TCAM = new ArrayList<>();
    }

    /**
     * Constructor no parametrizado del Nodo
     */
    public Node(){
        this.name = "";
        this.ID = -1;
        this.TCAM = new ArrayList<>();
    }

    /**
     * Devuelve la TCAM entera del nodo
     * @return Una Lista de instrucciones instaladas en el nodo
     */
    public ArrayList<Instruction> getTCAM() {
        return TCAM;
    }

    /**
     * Añade una solicitud SFC al nodo
     * @param Ins La instrucción a añadir
     */
    public void addInstruction(Instruction Ins){
        TCAM.add(Ins);
    }

    /**
     * Asocia un VNF al nodo
     * @param v El VNF a asociar
     */
    public void addVNF(VNF v){ VNFs.add(v);}

    /**
     * Devuelve el identificador del nodo
     * @return El identificador del nodo
     */
    public int getID() {
        return ID;
    }

    /**
     * Retorna la lista de VNF asociadas
     * @return Una Lista de VNF
     */
    public List<VNF> getVNFs() {
        return VNFs;
    }

    /**
     * Obtiene el VNF según el tipo de SFC que comprende
     * @param type Un Entero indicando el tipo de SFC
     * @return El VNF indicado, si existe
     */
    public VNF getVNFByType(int type){
        VNF n = new VNF(new Node(), 0, 0);
        for (VNF v:
             getVNFs()) {
            if(v.getSFCtype() == type){
                n = v;
            }
        }
        return n;
    }

    /**
     * Elimina la instrucción de la TCAM y de los VNF asociados del nodo
     * @param ins La instrucción a borrar
     */
    public void deleteIns(Instruction ins){

        for (Instruction in:
             TCAM) {
            if (ins.equals(in)){
                TCAM.remove(ins);
                for (VNF v: getVNFs()){
                   if (v.getSFCtype() == ins.getSFCactual()){
                       v.removeSFC(ins);
                       break;
                   }
                }
                break;
            }
        }

    }

    /**
     * Establece el total de memoria de la TCAM
     * @param memorySizeTotal La cantidad de memoria a establecer
     */
    public void setMemorySizeTotal(int memorySizeTotal) {
        MEMORY_SIZE_TOTAL = memorySizeTotal;
    }
}
