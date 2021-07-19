package src;

/**
 * Clase utilizada para manejar las descripciones SFC. Simplemente almacena en un POJO
 * ciertos atributos para manejar las cadenas de manera más cómoda.
 */
public class SFC_Desc {
    private int ID;
    private int type;
    private int n_instance;

    /**
     * Constructor parametrizado.
     * @param ID Identificador de la clase. Será el mismo para identificar el archivo de cadenas pertinente.
     * @param type Tipo de SFC.
     * @param n_instance Número de instancias de ese SFC en la red
     */
    public SFC_Desc(int ID, int type, int n_instance){
        this.ID = ID;
        this.type = type;
        this.n_instance = n_instance;
    }

    /**
     * Devuelve el identificador para saber la posición de la cadena
     * @return Un entero con el identificador de la cadena
     */
    public int getID() {
        return ID;
    }

    /**
     * Devuelve el tipo de SFC
     * @return Un entero con el tipo de SFC
     */
    public int getType() {
        return type;
    }


}
