package src;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class InitData {

    Graph graph;

    public InitData() {

        /**
         * type 0 = normal
         * type 1 = IN
         * type 2 = EN
         * type 3 = FW
         * type 4 = IDS
         * type 5 = NAT
         * type 6 = PROXY
         *
         */
        graph = new Graph();

    }

    public Graph init() throws IOException {

        graph.setNodes(initNodes());
        graph.setLinks(initLinks());
        initVNFs();

        return graph;
    }

    public ArrayList initNodes() throws IOException {
        ArrayList<Node> nodes = new ArrayList<>();
        String row;
        BufferedReader csvReader = new BufferedReader(new FileReader("DataSet/N.csv"));
        while ((row = csvReader.readLine()) != null) {
            String[] data = row.split(",");
            int NON = Integer.parseInt(data[0]);
            System.out.println(NON);
            for (int i = 1; i <= NON; i++){
                nodes.add(new Node(Character.toString((char) i+64), i));
            }

        }
        csvReader.close();
        return nodes;
    }

    public ArrayList initLinks() throws IOException {
        ArrayList<Link> links = new ArrayList<>();
        String row;
        BufferedReader csvReader = new BufferedReader(new FileReader("DataSet/linkRete.csv"));
        while ((row = csvReader.readLine()) != null) {
            String[] data = row.split(",");
            links.add(new Link(Integer.parseInt(data[0]), graph.searchByID(Integer.parseInt(data[1])), graph.searchByID(Integer.parseInt(data[2])), Integer.parseInt(data[3])));

        }
        csvReader.close();


        return links;
    }

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

    public ArrayList initInstructions(int i) throws IOException {
        ArrayList<Instruction> instructions = new ArrayList<>();
        String row;
        BufferedReader csvReader = new BufferedReader(new FileReader("DataSet/R_" + i + ".csv"));
        while ((row = csvReader.readLine()) != null) {
            String[] data = row.split(",");
            ArrayList<Integer> slots = new ArrayList<>();
            for (int j = 5; j < 15; j++){
                slots.add(Integer.parseInt(data[i]));
            }

            instructions.add(new Instruction(Integer.parseInt(data[0]),
                    graph.searchByID(Integer.parseInt(data[1])),
                    graph.searchByID(Integer.parseInt(data[2])),
                    Integer.parseInt(data[3]),
                    Integer.parseInt(data[4])));
            instructions.get(instructions.size()-1).setSlots(slots);

        }
        csvReader.close();

        return instructions;
    }

}
