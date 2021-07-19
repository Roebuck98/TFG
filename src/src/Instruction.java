package src;

import java.util.ArrayList;
import java.util.List;

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

    public int getSFC() {
        return SFC;
    }

    public Node getEN() {return EN;}

    public Node getIN() {return IN;}

    public void setSlots(List<Integer> slots) {
        this.slots = slots;
    }

    public int getID() {
        return ID;
    }

    public Node getActual() {
        return actual;
    }

    public void setActual(Node actual) {
        this.actual = actual;
    }

    public void setSFCactual(int SFCactual) {
        this.SFCactual = SFCactual;
        //MAX_INACTIVE = MAX_INACTIVE + 1;
    }

    public int getSFCactual() {
        return SFCactual;
    }

    public double getTCAM() {
        return TCAM;
    }

    public void setCi(List<CInstance> ci) {
        this.ci = ci;
    }

    public List<CInstance> getCi() {
        return ci;
    }

    public List<Integer> getSlots() {
        return slots;
    }

    public int getInactive() {
        return inactive;
    }

    public int getMAX_INACTIVE() {
        return MAX_INACTIVE;
    }

    public void setMAX_INACTIVE(int MAX_INACTIVE) {
        this.MAX_INACTIVE = MAX_INACTIVE;
    }

    public void setInactive(int inactive) {
        this.inactive = inactive;
    }

    public boolean isInstalled() {
        return installed;
    }

    public void setInstalled(boolean installed) {
        this.installed = installed;
    }

    public void setSatisfied(boolean satisfied) {
        this.satisfied = satisfied;
    }

    public List<Node> getPath() {
        return path;
    }

    public void addNodeToPath(Node n){
        path.add(n);
    }

    public boolean isSatisfied() {
        return satisfied;
    }

    public void setInstallNode(Node installNode) {
        this.installNode = installNode;
    }

    public Node getInstallNode() {
        return installNode;
    }
}
