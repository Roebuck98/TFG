package src;

import java.io.IOException;
import java.util.*;

public class App {
    static Graph graph;
    public static void main(String[] args) throws IOException {
        
        InitData initdata = new InitData();

        graph = initdata.init();


        List <Instruction> ins = initdata.initInstructions(1);

        /*List<Node> nodes = new ArrayList<>();


        nodes.addAll(graph.getNodes());

        Node init = graph.searchByID(1);
        Node end = graph.searchByID(12);
        List<Path>  paths= new DefaultKShortestPathFinder()
                .findShortestPaths(init, end, graph, 5);

        for (Path path : paths) {
            for (Node n:
                 path.getNodeList()) {
                System.out.print(n.getName() + ", ");
            }
            System.out.println(" " + path.pathCost() + " " + (path.getLoad()/path.getMAX_LOAD())*100 + "%" + ", MLU = " + path.getMLU());
        }*/

        exec(ins, graph);

    }

    public static void exec(List<Instruction> ins, Graph graph){

        for (int i = 0; i < ins.size(); i++) {
            checkIdleTimeout(ins);

            Instruction inst = ins.get(i);

            List<Path>  paths = new DefaultKShortestPathFinder().findShortestPaths(inst.getIN(), inst.getEN(), graph, 1);
            Path path;
            path = paths.get(0);

            Node instNode = installRule(path, inst);
            if(instNode.getID() != -1){

            }

        }

    }

    public static Node installRule(Path path, Instruction ins){
        Boolean installed = false;
        Node inInst = new Node();

        for (int i = 0; i < path.getNodeList().size() && !installed; i++) {
            if(path.getNodeList().get(i).getTCAM().size() < path.getNodeList().get(i).MEMORY_SIZE_TOTAL){
                path.getNodeList().get(i).addInstruction(ins);
                installed = true;
                inInst = path.getNodeList().get(i);
                System.out.println("Instalada la instrucción " + ins.getID() + " en el nodo " + inInst.getID());
                ins.setActual(inInst);
            }
        }
        if(!installed){
            System.out.println("No se ha podido instalar la Instrucción " + ins.getID() + ", se procederá a su descarte");
        }

        return inInst;

    }

    public static void checkIdleTimeout(List<Instruction> ins){

    }

    public static void nextJump (Instruction ins){
        List<Node> nodes = new ArrayList<>();
        int MLU = 0;
        Path def = new Path(ins.getActual());
        if(ins.getSFCactual() == ins.getSFC()){
            List<Path>  paths= new DefaultKShortestPathFinder().findShortestPaths(ins.getActual(), ins.getEN(), graph, 5);

            for (Path path : paths) {
                if(!checkLoops(path)){
                    if (path.getMLU() < MLU){
                        def = path;
                    }
                }

            }

            //Actualizar paths
        }
        else{
            nodes = graph.getNodesBySFC(ins.getSFCactual()+1);
            for (Node n: nodes) {
                List<Path>  paths= new DefaultKShortestPathFinder().findShortestPaths(ins.getActual(), n, graph, 5);
                def = paths.get(0);
                for (Path path : paths) {
                    if(!checkLoops(path)){
                        if (path.getMLU() < MLU){
                            def = path;
                        }
                    }

                }

            }
            ins.setSFCactual(ins.getSFCactual()+1);
            ins.setActual(def.getEndNode());
            for (Node n: def.getNodeList()) {
                //actualizar links
            }


        }

    }

    public static Boolean checkLoops(Path path){
        boolean loop = false;

        for (int i = 0; i < path.getNodeList().size() && !loop; i++){
           if(path.getNodeList().get(i).equals(path.getNodeList().get(i+2))){
               loop = true;
           }
        }

        return loop;
    }
    
}