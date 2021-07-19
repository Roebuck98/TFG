package src;

import java.util.*;

public class Node {

    private String name;
    private int ID;
    private ArrayList<Instruction> TCAM;
    protected int MEMORY_SIZE_TOTAL;
    private List <Instruction> buffer = new LinkedList<>();
    private List <VNF> VNFs = new LinkedList<>();


    public Node(String name, int ID){
        this.name = name;
        this.ID = ID;
        this.TCAM = new ArrayList<>();
    }

    public Node(){
        this.name = "";
        this.ID = -1;
        this.TCAM = new ArrayList<>();
    }

    public ArrayList<Instruction> getTCAM() {
        return TCAM;
    }

    public void addInstruction(Instruction Ins){
        TCAM.add(Ins);
    }

    public void addVNF(VNF v){ VNFs.add(v);}


    public int getID() {
        return ID;
    }

    public List<VNF> getVNFs() {
        return VNFs;
    }

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

    public void setMemorySizeTotal(int memorySizeTotal) {
        MEMORY_SIZE_TOTAL = memorySizeTotal;
    }
}
