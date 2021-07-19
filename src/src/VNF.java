package src;

import java.util.ArrayList;
import java.util.List;

public class VNF {
    Node n;
    int SFCtype;
    double bandwidth;
    double actualbd;
    List <Instruction> SFC;

    public VNF(Node n, int SFCtype, double bandwidth){
        this.n = n;
        this.SFCtype = SFCtype;
        this.bandwidth = bandwidth;
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

    public void removeSFC(Instruction ins){
        SFC.remove(ins);
    }

}
