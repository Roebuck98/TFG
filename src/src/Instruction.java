package src;

import java.util.ArrayList;
import java.util.List;

public class Instruction {

    private int ID;

    private Node IN;
    private Node EN;

    private int SFC;

    private int TCAM;

    private Node actual;
    private int SFCactual;

    private List<Integer> slots = new ArrayList<>();

    public Instruction(int ID, Node IN, Node EN, int TCAM, int SFC){
        this.ID = ID;
        this.IN = IN;
        this.EN = EN;
        this.SFC = SFC;
        this.TCAM = TCAM;
        SFCactual = 0;
    }

    public int getSFC() {
        return SFC;
    }

    public Node getEN() {return EN;}

    public Node getIN() {return IN;}

    public void setEN(Node EN) {this.EN = EN;}

    public void setIN(Node IN) {this.IN = IN;}

    public void addSlot(int slot){
        slots.add(slot);
    }

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
    }

    public int getSFCactual() {
        return SFCactual;
    }
}
