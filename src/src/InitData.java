package src;

import java.io.*;
import java.util.*;

/**
 * Clase para inicializar los datos de sus respectivos archivos
 */
public class InitData {

    Graph graph;
    int mem;
    int idle_timeout;
    int prob;

    /**
     * Inicializa el grafo
     */
    public InitData() {

        graph = new Graph();

    }

    /**
     * Carga y añade los Nodos y los Enlaces al grafo. Inicializa también los VNFs.
     * Carga el archivo de configuración que indica el Idle Timeout estático
     * y el tamaño de las TCAM de los nodos.
     * @return El grafo ya preparado
     * @throws IOException Si los archivos de carga no se encuentran
     */
    public Graph init() throws IOException {

        String row;
        BufferedReader csvReader = new BufferedReader(new FileReader("DataSet/config.csv"));
        while ((row = csvReader.readLine()) != null) {
            String[] data = row.split(",");
            mem = Integer.parseInt(data[1]);
            idle_timeout = Integer.parseInt(data[0]);
            prob = Math.round(100 / Integer.parseInt(data[2]));
        }
        csvReader.close();


        graph.setNodes(initNodes());
        graph.setLinks(initLinks());
        initVNFs();

        return graph;
    }

    /**
     * Inicializa los nodos. Busca un archivo que indica el número de nodos de la red, y crea ese mismo
     * número de objetos Nodo.
     * @return Una lista con todos los nodos cargados
     * @throws IOException Si el archivo N.csv no se encuentra
     */
    public ArrayList initNodes() throws IOException {
        ArrayList<Node> nodes = new ArrayList<>();
        String row;
        BufferedReader csvReader = new BufferedReader(new FileReader("DataSet/N.csv"));
        while ((row = csvReader.readLine()) != null) {
            String[] data = row.split(",");
            int NON = Integer.parseInt(data[0]);
            for (int i = 1; i <= NON; i++){
                nodes.add(new Node(Character.toString((char) i+64), i));
                nodes.get(nodes.size()-1).setMemorySizeTotal(mem);
            }

        }
        csvReader.close();
        return nodes;
    }

    /**
     * Inicializa y carga los enlaces leyendo los datos de un archivo indicado.
     * Los nodos asociados se buscan en el grafo, por lo que se tienen que inicializar previamente.
     * @return Una lista con todos los Links, cargados
     * @throws IOException Si el archivo linkRete.csv no se encuentra
     */
    public ArrayList initLinks() throws IOException {
        ArrayList<Link> links = new ArrayList<>();
        String row;
        BufferedReader csvReader = new BufferedReader(new FileReader("DataSet/linkRete.csv"));
        while ((row = csvReader.readLine()) != null) {
            String[] data = row.split(",");
            links.add(new Link(Integer.parseInt(data[0]), graph.searchByID(Integer.parseInt(data[1])),
                    graph.searchByID(Integer.parseInt(data[2])), Double.parseDouble(data[3])));
        }
        csvReader.close();


        return links;
    }

    /**
     * Inicializa los VNF. Usa el grafo para buscar los nodos pertinentes, por lo que el grafo tiene que cargar sus datos previamente.
     * @throws IOException Si el archivo VNF_cap.csv no se encuentra
     */
    public void initVNFs() throws IOException {
        String row;
        BufferedReader csvReader = new BufferedReader(new FileReader("DataSet/VNF_cap.csv"));
        while ((row = csvReader.readLine()) != null) {
            String[] data = row.split(",");
            Node aux;
            aux = graph.searchByID(Integer.parseInt(data[0]));
            aux.addVNF(new VNF(aux, Integer.parseInt(data[1]), Integer.parseInt(data[2])));

        }
        csvReader.close();

    }

    /**
     * Lee y carga una batería de solicitudes SFC.
     * Primero, carga el archivo según un índice i, ya que se dispone de 5 baterías de solicitudes.
     * Después. se carga en una lista los slots de tiempo indicados.
     * Finalmente, se crean los objetos Instructions con la lista de slots y los Nodos indicados que se han buscado en el grafo.
     * @param i Indica el archivo del que se va a cargar la batería de SFCs
     * @return Una lista con todas las instrucciones cargadas
     * @throws IOException Si el archivo R_+i+.csv  no se encuentra
     */
    public ArrayList initInstructions(int i) throws IOException {
        ArrayList<Instruction> instructions = new ArrayList<>();
        String row;
        BufferedReader csvReader = new BufferedReader(new FileReader("DataSet/R_" + i + ".csv"));
        while ((row = csvReader.readLine()) != null) {
            String[] data = row.split(",");
            ArrayList<Integer> slots = new ArrayList<>();
            int counter = 0;
            for (int j = 5; j < 15; j++){
                slots.add(Integer.parseInt(data[j]));
                //slots.add((int)Math.floor(Math.random()*(1-0+1)+0));

                /*if(counter < prob){
                    slots.add(1);
                    counter ++;
                }else{
                    slots.add(0);
                }
                Collections.shuffle(slots);*/
            }



            instructions.add(new Instruction(Integer.parseInt(data[0]),
                    graph.searchByID(Integer.parseInt(data[1])),
                    graph.searchByID(Integer.parseInt(data[2])),
                    Double.parseDouble(data[3]),
                    Integer.parseInt(data[4])));
            instructions.get(instructions.size()-1).setSlots(slots);

            instructions.get(instructions.size()-1).setMAX_INACTIVE(idle_timeout);

        }
        csvReader.close();

        return instructions;
    }

    /**
     * Inicializa el descriptor de los SFC, que indica las cadenas de instancia pàra facilitar la búsqueda de caminos.
     * @return Una lista con los descriptores SFC
     * @throws IOException Si el archivo SFC_description.csv no se encuentra
     */
    public ArrayList initSFCDesc () throws IOException {
        ArrayList<SFC_Desc> sfcd = new ArrayList<>();
        String row;
        BufferedReader csvReader = new BufferedReader(new FileReader("DataSet/SFC_description.csv"));
        while ((row = csvReader.readLine()) != null) {
            String[] data = row.split(",");
            sfcd.add(new SFC_Desc(Integer.parseInt(data[0]), Integer.parseInt(data[1]), Integer.parseInt(data[2])));
        }
        csvReader.close();
        return sfcd;
    }

    /**
     * Inicializa las cadenas de instancia.
     * Va leyendo cada uno de los archivos para cargar el camino posible en una lista.
     * Cuando va terminando con dicha lista, la guarda en un mapa.
     * @return Un Mapa con todos los caminos posibles
     * @throws IOException Si los archivos chain_instance_+i+.csv no se encuentran
     */
    public Map initChains () throws IOException{
        Map<Integer, List> chains = new HashMap<>();
        String row;
        for (int i = 1; i > 0; i++){
            File csvFile = new File("DataSet/chain_instance_"+i+".csv");
            List<CInstance> ci;
            if (csvFile.isFile()) {
                BufferedReader csvReader = new BufferedReader(new FileReader("DataSet/chain_instance_"+i+".csv"));
                ci = new ArrayList<>();
                while ((row = csvReader.readLine()) != null) {
                    String[] data = row.split(",");

                    ci.add(new CInstance(graph.searchByID(Integer.parseInt(data[0])), Integer.parseInt(data[1])));
                }
                chains.put(i, ci);
                csvReader.close();
            }else{
                break;
            }
        }
        return chains;
    }

    public int getIdle_timeout() {
        return idle_timeout;
    }
}
