package src;

import java.util.ArrayList;
import java.util.List;

public class VNF {
    Node n;
    int SFCtype;
    int bandwith;
    int actualbd;
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

    public void setActualbd(int actualbd) {
        this.actualbd = actualbd;
    }

    public int getActualbd() {
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
}
