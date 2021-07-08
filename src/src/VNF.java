package src;

import java.util.ArrayList;
import java.util.List;

public class VNF {
    Node n;
    int SFCtype;
    int bandwith;
    double actualbd;
    List <Instruction> SFC;

    public VNF(Node n, int SFCtype, int bandwith){
        this.n = n;
        this.SFCtype = SFCtype;
        this.bandwith = bandwith;
        SFC = new ArrayList<>();
        actualbd = 0;
    }

    public int getSFCtype() {
        return SFCtype;
    }

    public void addSFC(Instruction ins){
        SFC.add(ins);
    }

    public void setActualbd(double actualbd) {
        this.actualbd = actualbd;
    }

    public double getActualbd() {
        return actualbd;
    }

    public Instruction searchByID(int ID){
        Instruction ins = new Instruction(-1, new Node(), new Node(), 0,0);
        for (Instruction i: SFC) {
            if (i.getID() == ID){
                ins = i;
            }
        }
        return ins;
    }

    public void removeSFC(Instruction ins){
        SFC.remove(ins);
    }

}
