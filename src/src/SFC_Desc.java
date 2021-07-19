package src;

public class SFC_Desc {
    private int ID;
    private int type;
    private int n_instance;

    public SFC_Desc(int ID, int type, int n_instance){
        this.ID = ID;
        this.type = type;
        this.n_instance = n_instance;
    }

    public int getID() {
        return ID;
    }

    public int getType() {
        return type;
    }


}
