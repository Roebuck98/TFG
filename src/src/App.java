package src;

import java.io.IOException;
import java.util.*;

public class App {
    static Graph graph;
    static List <Instruction> ins;
    static List<SFC_Desc> sfcd;
    static Map <Integer, List<CInstance>> chains;
    static List <Instruction> installed = new ArrayList<>();
    static List <Instruction> removed = new ArrayList<>();
    static List<Instruction> satisfied = new ArrayList<>();
    public static void main(String[] args) throws IOException {
        
        InitData initdata = new InitData();

        graph = initdata.init();
        ins = initdata.initInstructions(1);
        sfcd = initdata.initSFCDesc();
        chains = initdata.initChains();

        for (int i = 0; i < 10; i++) {
            System.out.println("Slot numero: " + i + " /////////////////////////////////////////////////");
            exec(ins, i);
        }


        System.out.println("Reglas instaladas: " + installed.size());
        System.out.println("Reglas descartadas: " + removed.size());
        System.out.println("Reglas satisfechas: " + satisfied.size());
        System.out.println("Reglas restantes: " + ins.size());



    }

    public static void exec(List<Instruction> ins, int slot){

        //Instrucción por Instruccion
        for (int i = 0; i < ins.size(); i++) {


            Instruction inst = ins.get(i);

            if (inst.getSlots().get(slot) == 1){
                inst.setInactive(0);

                if (!inst.isInstalled()){
                    //Instalación de la regla en el nodo
                    Node instNode = installRule(inst);
                    //En el caso de que se haya instalado
                    if(instNode.getID() != -1){
                        System.out.println("Instalada la instrucción " + inst.getID() + " en el nodo " + instNode.getID());
                        installed.add(inst);
                       inst.setInstalled(true);
                    }
                }
                else {
                    nextJump(inst);
                }

            }
            else{
                if(inst.isInstalled()){
                    inst.setInactive(inst.getInactive() + 1);
                    //Regla no satisfecha e inactiva
                    //si no sigue activa y expira el idle timeout, se elimina tanto la regla como el tráfico del camino.
                    if (inst.getInactive() == inst.getMAX_INACTIVE()){
                        System.out.println("Se ha descartado la regla: "+ inst.getID() +" por inactiva");
                        discardSFC(inst.getID());
                        updateLinks(inst.getPath(), inst, 1);

                    }else{
                        //Regla satisfecha pero inactiva
                        // si no sigue activa y no expira el timeout, se elimina el tráfico del camino
                        if(inst.getSFC() == inst.getSFCactual()){
                            //updateLinks(inst.getPath(), inst, 1);
                            System.out.println("La solicitud SFC " + inst.getID() + " ha sido satisfecha correctamente. -----------------------------");
                            satisfied.add(inst);
                            inst.getActual().deleteIns(inst);
                            ins.remove(inst);
                            updateLinks(inst.getPath(), inst, 1);
                        }
                    }



                }
            }



        }

    }

    public static Node installRule(Instruction ins){
        Boolean installed = false;
        Node inInst = new Node();
        List<Path>  paths= new DefaultKShortestPathFinder().findShortestPaths(ins.getIN(), ins.getEN(), graph, 5);
        List<Node> nodes = new ArrayList<>();

        for (Path path: paths) {

            for (int i = 0; i < path.getNodeList().size() && !installed; i++) {
                if(path.getNodeList().get(i).getTCAM().size() < path.getNodeList().get(i).MEMORY_SIZE_TOTAL){
                    path.getNodeList().get(i).addInstruction(ins);
                    installed = true;
                    inInst = path.getNodeList().get(i);
                    ins.setActual(inInst);
                    ins.addNodeToPath(ins.getIN());
                    if (!inInst.equals(ins.getIN())){
                        for (int j = 0; j<=i; j++){
                            nodes.add(path.getNodeList().get(j));
                            ins.addNodeToPath(path.getNodeList().get(j));
                        }
                        updateLinks(nodes, ins, 0);

                    }

                }
            }

            if (installed) break;
        }
        if(!installed){
            System.out.println("No se ha podido instalar la Instrucción " + ins.getID() + ", se procederá a su descarte");
            discardSFC(ins.getID());
        }

        return inInst;

    }

    public static void nextJump (Instruction ins){
        Path path;
        if(ins.getCi().size() == 0){
            assignChain(ins);
            System.out.println("Cadena: ");
            for (CInstance c:
                 ins.getCi()) {
                System.out.println("Nodo: " + c.getN().getID() + ", SFC: " + c.getSFC_type());
            }
        }
        //Saltar por los VNF o al EN

        if (ins.getSFCactual() == ins.getSFC()){
            path = getKSPLowMLU(ins.getActual(), ins.getEN());
            if (path.getMLU() < 100.0){
                ins.setSatisfied(true);
            }
            else{
                System.out.println("La regla " + ins.getID() + " no ha podido instalarse en el EN.");
            }


        }else{
            //Para actualizar la capacidad de los enlaces

            //Se desliga el valor de la memoria y la lista de SFC del VNF
            updateVNF(ins, 1);

            if (!ins.getCi().get(ins.getSFCactual()).getN().equals(ins.getActual())){
                path = getKSPLowMLU(ins.getActual(), ins.getCi().get(ins.getSFCactual()).getN());
                for (Node n: path.getNodeList()) {
                    if (!n.equals(ins.getPath().get(ins.getPath().size()-1))){
                        ins.addNodeToPath(n);
                    }

                }
                updateLinks(path.getNodeList(), ins, 0);
            }
            ins.setSFCactual(ins.getSFCactual()+1);

            //Se actualizan los valores del nuevo VNF, su bandwidth y su lista de SFCs
            updateVNF(ins, 0);

        }


    }

    public static Boolean checkLoops(Path path){
        boolean loop = false;

        for (int i = 0; i < path.getNodeList().size()-2 && !loop; i++){
           if(path.getNodeList().get(i).equals(path.getNodeList().get(i+2))){
               loop = true;
           }
        }

        return loop;
    }

    public static void discardSFC(int ID){
        Instruction i;
        boolean deleted = false;
        for (int j = 0; j < ins.size() && !deleted; j++) {
            i = ins.get(j);
            if (i.getID() == ID){
                removed.add(i);
                ins.remove(i);
                deleted = true;
                System.out.println("Se ha borrado la instruccion: "+ i.getID() + " correctamente.");
            }

        }
    }

    public static Path getKSPLowMLU(Node a, Node b){
        Path path = new Path(a);
        double MLU = 100.0;

        List<Path>  paths= new DefaultKShortestPathFinder()
                .findShortestPaths(a, b, graph, 15);
        for (Path p : paths) {
           if(!checkLoops(p)){
                if(p.getMLU() < MLU){
                    path = p;
                    MLU = p.getMLU();
                }
           }
        }
        return path;
    }

    public static void assignChain(Instruction ins){
        Path path = new Path(ins.getActual());
        path.setMLU(100.0);

        for (int i = 0; i<sfcd.size(); i++){
            if(ins.getSFC() == sfcd.get(i).getType()){
                Path p2 = getKSPLowMLU(ins.getActual(), chains.get(sfcd.get(i).getID()).get(0).getN());
                if(path.getMLU() >= p2.getMLU()){
                    path = p2;
                    ins.setCi(chains.get(sfcd.get(i).getID()));
                }
            }
        }


    }

    public static void updateLinks(List<Node> nodes, Instruction ins, int add_or_sub){

            for (int j = 0; j < nodes.size()-1; j++){
                Link l = graph.searchLink(nodes.get(j), nodes.get(j+1));
                Link l2 = graph.searchLink(nodes.get(j+1), nodes.get(j));

                if (l!=null && l2 != null){
                    switch (add_or_sub){
                        case 0:
                            l.addBW(ins.getTCAM());
                            l2.addBW(ins.getTCAM());
                            break;
                        case 1:
                            l.subBW(ins.getTCAM());
                            l2.subBW(ins.getTCAM());
                            break;

                    }

                    System.out.println(l.getA().getID() + " " + l.getBandwidth()+ " " + l.getB().getID());
                }



            }

    }

    public static void updateVNF(Instruction ins, int add_or_remove){

        switch (add_or_remove){
            case 0:
                ins.getActual().getVNFByType(ins.getSFCactual()).setActualbd(ins.getActual().getVNFByType(ins.getSFCactual()).getActualbd()+ins.getTCAM());
                ins.getActual().getVNFByType(ins.getSFCactual()).addSFC(ins);
                break;
            case 1:
                ins.getActual().getVNFByType(ins.getSFCactual()).setActualbd(ins.getActual().getVNFByType(ins.getSFCactual()).getActualbd()-ins.getTCAM());
                ins.getActual().getVNFByType(ins.getSFCactual()).removeSFC(ins);
                break;

        }


    }

    public static void test(){
                List<Node> nodes = new ArrayList<>();


        nodes.addAll(graph.getNodes());

        Node init = graph.searchByID(1);
        Node end = graph.searchByID(12);
        List<Path>  paths= new DefaultKShortestPathFinder()
                .findShortestPaths(init, end, graph, 15);

        for (Path path : paths) {
            for (Node n:
                 path.getNodeList()) {
                System.out.print(n.getID() + ", ");
            }
            System.out.println(" " + path.pathCost() + " " + (path.getLoad()/path.getMAX_LOAD())*100 + "%" + ", MLU = " + path.getMLU());
        }
    }

}