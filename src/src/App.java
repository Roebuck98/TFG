package src;

import java.io.IOException;
import java.util.*;

public class App {

    static Graph graph;
    static List <Instruction> ins;
    static List<SFC_Desc> sfcd;
    static int staticIdle;
    static Map <Integer, List<CInstance>> chains;
    static List <Instruction> installed = new ArrayList<>();
    static List <Instruction> removed = new ArrayList<>();
    static List<Instruction> satisfied = new ArrayList<>();


    /**
     * Clase principal del algoritmo
     *
     * @param args
     * @throws IOException
     */

    public static void main(String[] args) throws IOException {
        
        InitData initdata = new InitData();

        graph = initdata.init();
        ins = initdata.initInstructions(5);
        sfcd = initdata.initSFCDesc();
        chains = initdata.initChains();
        staticIdle = initdata.getIdle_timeout();


        for (int i = 0; i < 10; i++) {
            System.out.println("Slot numero: " + i + " /////////////////////////////////////////////////");
            exec(ins, i);
        }

        System.out.println("Reglas instaladas: " + installed.size());
        System.out.println("Porcentaje de instalación: " + installed.size()*100.0/ (removed.size()+satisfied.size()+ins.size()));
        System.out.println("Reglas descartadas: " + removed.size());
        System.out.println("Reglas satisfechas: " + satisfied.size());
        System.out.println("Reglas restantes: " + ins.size());
    }

    /**
     * Módulo que contempla las acciones si una solicitud está activa o inactiva, además de
     * comprobar las condiciones de instalación y satisfacción.
     * @param ins La instrucción a tratar
     * @param slot El slot de tiempo activo o inactivo
     */
    public static void exec(List<Instruction> ins, int slot){

        //Instrucción por Instruccion
        for (int i = 0; i < ins.size(); i++) {
            Instruction inst = ins.get(i);
            System.out.println("INICIO DE INSTRUCCIÓN: " + inst.getID());
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

                    }else{
                        //Regla satisfecha pero inactiva
                        // si no sigue activa y no expira el timeout, se elimina el tráfico del camino
                        if(inst.isSatisfied()){
                            //updateLinks(inst.getPath(), inst, 1);
                            System.out.println("La solicitud SFC " + inst.getID() + " ha sido satisfecha correctamente. *******************************");
                            satisfied.add(inst);
                            inst.getInstallNode().deleteIns(inst);
                            ins.remove(inst);
                            updateLinks(inst.getPath(), inst, 1);
                        }
                    }

                }
            }
            System.out.println("FIN DE INSTRUCCIÓN: " + inst.getID());
        }

    }

    /**
     * Instala una solicitud en un nodo dado un camino ópyimo. Si no encuentra en ninguno de los caminos un
     * nodo con ranura disponible o un camino con menos de 100 de MLU, la instrucción se descarta
     * @param ins La instrucción a instalar
     * @return El nodo donde se ha instalado la solicitud SFC
     */
    public static Node installRule(Instruction ins){
        Boolean installed = false;
        Node inInst = new Node();
        List<Path>  paths= new DefaultKShortestPathFinder().findShortestPaths(ins.getIN(), ins.getEN(), graph, 5);

        for (Path path: paths) {

            for (int i = 0; i < path.getNodeList().size() && !installed; i++) {
                if(path.getNodeList().get(i).getTCAM().size() < path.getNodeList().get(i).MEMORY_SIZE_TOTAL
                        && graph.checkAvailablePath(path.getNodeList(), ins.getTCAM())){
                    path.getNodeList().get(i).addInstruction(ins);
                    installed = true;
                    inInst = path.getNodeList().get(i);
                    ins.setActual(inInst);
                    ins.setInstallNode(inInst);
                    ins.addNodeToPath(ins.getIN());
                    if (!inInst.equals(ins.getIN())){
                        for (int j = 1; j<=i; j++){
                            ins.addNodeToPath(path.getNodeList().get(j));
                        }
                        updateLinks(ins.getPath(), ins, 0);
                        evaluateTimeout(ins);
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

    /**
     * Opera el siguiente salto de la instrucción. Si es su primer salto desde el nodo clasificador, le asigna una cadena.
     * Si es un salto entre cadenas, identifica si se tiene que quedar en el mismo nodo o ir a otro.
     * También identifica cuando la cadena se termina y cuando tiene que saltar al Egress Node
     * @param ins La instrucción a tratar.
     */
    public static void nextJump (Instruction ins){
        Path path;
        if(ins.getCi().size() == 0){
            assignChain(ins);
            System.out.println("Cadena de la instrucción " +ins.getID() + ": ");
            for (CInstance c:
                 ins.getCi()) {
                System.out.println("Nodo: " + c.getN().getID() + ", SFC: " + c.getSFC_type());
            }
        }
        //Saltar por los VNF o al EN

        if (ins.getSFCactual() == ins.getSFC()){
            path = getKSPLowMLU(ins.getActual(), ins.getEN());
            if (graph.checkAvailablePath(path.getNodeList(), ins.getTCAM())){
                ins.setSatisfied(true);
                ins.setActual(ins.getEN());

                for (int j = 1; j< path.getNodeList().size(); j++){
                    ins.addNodeToPath(path.getNodeList().get(j));
                }
                updateLinks(path.getNodeList(), ins, 0);

            }
            else{
                System.out.println("La regla " + ins.getID() + " no ha podido instalarse en el EN.");
                discardSFC(ins.getID());

            }


        }
        else{
            //Para actualizar la capacidad de los enlaces

            //Se desliga el valor de la memoria y la lista de SFC del VNF
            if (ins.getSFCactual()>0){
                updateVNF(ins, 1);
            }

            if (!ins.getCi().get(ins.getSFCactual()).getN().equals(ins.getActual())){
                path = getKSPLowMLU(ins.getActual(), ins.getCi().get(ins.getSFCactual()).getN());

                if (graph.checkAvailablePath(path.getNodeList(), ins.getTCAM())){

                    for (int j = 1; j < path.getNodeList().size(); j++){
                        ins.addNodeToPath(path.getNodeList().get(j));
                    }
                    updateLinks(path.getNodeList(), ins, 0);
                    ins.setSFCactual(ins.getSFCactual()+1);
                    ins.setActual(path.getEndNode());

                    //Se actualizan los valores del nuevo VNF, su bandwidth y su lista de SFCs
                    if (ins.getSFCactual() < ins.getSFC()){
                        System.out.println("Instrucción: " + ins.getID() + ", SFC: "+ins.getSFCactual() + " / " + ins.getSFC());
                        System.out.println("Nodo actual: " + ins.getActual().getID());
                        updateVNF(ins, 0);
                    }

                }
                else{
                    System.out.println("La regla " + ins.getID() + " no ha podido seguir la cadena, y por lo tanto, será descartada.");
                    discardSFC(ins.getID());

                }
            }else{
                ins.setSFCactual(ins.getSFCactual()+1);

                //Se actualizan los valores del nuevo VNF, su bandwidth y su lista de SFCs
                updateVNF(ins, 0);
            }

        }


    }

    /**
     * Comprueba que los caminos no tengan bucles innecesarios
     * @param path El camino a comprobar
     * @return True si el camino tiene bucles, False si no
     */
    public static Boolean checkLoops(Path path){
        boolean loop = false;

        for (int i = 2; i < path.getNodeList().size()-2 && !loop; i++){
           if(path.getNodeList().get(i).equals(path.getNodeList().get(i+2)) && path.getNodeList().get(i).equals(path.getNodeList().get(i-2))){
               loop = true;
           }
        }

        return loop;
    }

    /**
     * Descarta la instrucción de la simulación. Limpia los enlaces y VNF por donde pasaba la instrucción.
     * Agrefa la instrucción a la lista de descartes.
     * @param ID El identificador de la instrucción a eliminar
     */
    public static void discardSFC(int ID){
        Instruction i;
        boolean deleted = false;
        for (int j = 0; j < ins.size() && !deleted; j++) {
            i = ins.get(j);
            if (i.getID() == ID){
                removed.add(i);
                if (i.getPath().size() > 1){
                    updateLinks(i.getPath(), i, 1);
                }
                if (i.getInstallNode() != null){
                    i.getInstallNode().deleteIns(i);
                }
                ins.remove(i);
                deleted = true;
                System.out.println("Se ha borrado la instruccion: "+ i.getID() + " correctamente.");
            }

        }
    }

    /**
     * Busca un camino entre los K-shortest path que tenga el menor MLU posible
     * @param a El nodo inicial del camino
     * @param b El nodo destino del camino
     * @return El Path con menor MLU
     */
    public static Path getKSPLowMLU(Node a, Node b){
        Path path = null;

        List<Path>  paths= new DefaultKShortestPathFinder().findShortestPaths(a, b, graph, 15);
        for (Path p : paths) {
           if(!checkLoops(p)){
               if(path == null){
                   path = p;
               }else{
                   if(p.getMLU() < path.getMLU()){
                       path = p;
                   }
               }

           }
        }
        return path;
    }

    /**
     * Asigna una cadena SFC a una solicitud
     * @param ins La solicitud a la que la cadena se adhiere
     */
    public static void assignChain(Instruction ins){
        Path path = null;
        double MLU;
        double MLU2;

        for (int i = 0; i<sfcd.size() ; i++){
            if(ins.getSFC() == sfcd.get(i).getType()){
                Path p2 = getKSPLowMLU(ins.getActual(), chains.get(sfcd.get(i).getID()).get(0).getN());
                if(path == null){
                   path = p2;
                    ins.setCi(chains.get(sfcd.get(i).getID()));
                }
                else{
                    MLU = path.getMLU();
                    MLU2 = p2.getMLU();

                    if(MLU > MLU2){
                        path = p2;
                        //System.out.println(p2.getMLU());
                        ins.setCi(chains.get(sfcd.get(i).getID()));
                        break;
                    }
                }

            }
        }

    }

    /**
     * Actualiza la capacidad de los enlaces de la red según un camino dado. Añade o retira capacidad de flujo cuando sea necesario.
     * Avisa si existiese capacidad negativa, o si el enlace está por encima de su capacidad.
     * @param nodes La lista de nodos que conforman el camino.
     * @param ins La instrucción de dicho camino.
     * @param add_or_sub Flag que indica si hay que añadir capacidad o retirarla.
     */
    public static void updateLinks(List<Node> nodes, Instruction ins, int add_or_sub){

            for (int j = 0; j < nodes.size()-1; j++){
                Link l = graph.searchLink(nodes.get(j), nodes.get(j+1));

                if (l!=null){
                    switch (add_or_sub){
                        case 0:
                            l.addBW(ins.getTCAM());
                            break;
                        case 1:
                            l.subBW(ins.getTCAM());
                            break;

                    }

                    System.out.println(l.getA().getID() + " " + l.getBandwidth()+ " " + l.getB().getID());
                }
                else{
                    System.out.println("ESTE CAMINO NO ES COMPATIBLE()()()()()()()()()()()()()()()()()()()()()()()()()");
                }



            }

    }

    /**
     * Actualiza las capacidades de una VNF. Puede añadir o retirar según se indique.
     * Avisa si una capacidad es negativa o si se rebasa el límite.
     * @param ins La instrucción perteneciente a esa VNF.
     * @param add_or_remove Indica si hay que sumar o sustraer.
     */
    public static void updateVNF(Instruction ins, int add_or_remove){

        switch (add_or_remove){
            case 0:
                if (ins.getActual().getVNFByType(ins.getSFCactual()).getActualbd() + ins.getTCAM() < ins.getActual().getVNFByType(ins.getSFCactual()).getBandwidth()){
                    ins.getActual().getVNFByType(ins.getSFCactual()).setActualbd(ins.getActual().getVNFByType(ins.getSFCactual()).getActualbd()+ins.getTCAM());
                    ins.getActual().getVNFByType(ins.getSFCactual()).addSFC(ins);
                    System.out.println("Capacidad del VNF: " + ins.getActual().getVNFByType(ins.getSFCactual()).getActualbd() + " / " + ins.getActual().getVNFByType(ins.getSFCactual()).getBandwidth());
                }else{
                    System.out.println("SE HA SUPERADO EL LÍMITE DE MEMORIA DEL VNF, SE PROCEDERÁ AL DESCARTE:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::");
                    System.out.println("Capacidad del VNF: " + ins.getActual().getVNFByType(ins.getSFCactual()).getActualbd() + " / " + ins.getActual().getVNFByType(ins.getSFCactual()).getBandwidth());
                    discardSFC(ins.getID());
                }


                break;
            case 1:
                if (ins.getActual().getVNFByType(ins.getSFCactual()).getActualbd() - ins.getTCAM() >= 0.0){
                    ins.getActual().getVNFByType(ins.getSFCactual()).setActualbd(ins.getActual().getVNFByType(ins.getSFCactual()).getActualbd()-ins.getTCAM());
                    ins.getActual().getVNFByType(ins.getSFCactual()).removeSFC(ins);
                }else{
                    System.out.println("VALOR NEGATIVO DEL VNF, COMPRUEBE EL ERROR::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::");
                }


                break;

        }


    }

    /**
     *
     */
    public static void test(){
        List<Node> nodes = new ArrayList<>();


        nodes.addAll(graph.getNodes());

        Node init = graph.searchByID(12);
        Node end = graph.searchByID(1);
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

    /**
     *
     * @param ins
     */
    public static void evaluateTimeout(Instruction ins){
        double rejectedValue = 0;
        int counterReject = 0;
        double satisfiedValue = 0;
        int counterSatisfied = 0;
        int o = 0;
        /*Satisfechas = 2, rechazadas = 1
        */

        for (int i = 0; i < removed.size(); i++) {
            Instruction aux = removed.get(i);
            if (aux.getSFC() == ins.getSFC() && aux.getIN().equals(ins.getIN())
                    && aux.getEN().equals(ins.getEN()) && aux.isInstalled() && aux.MAX_INACTIVE == aux.getInactive()){
                //Idle Máximo
                rejectedValue = rejectedValue + aux.MAX_INACTIVE;
                counterReject++;
            }
        }
        if (counterReject > 0){
            rejectedValue = rejectedValue / counterReject;
            o = o + 1;
        }

        for (int i = 0; i < satisfied.size(); i++) {
            Instruction aux = satisfied.get(i);
            if (aux.getSFC() == ins.getSFC() && aux.getIN().equals(ins.getIN()) && aux.getEN().equals(ins.getEN())){
                //Idle Máximo
                satisfiedValue = satisfiedValue + aux.MAX_INACTIVE;
                counterSatisfied++;
            }
        }
        if (counterSatisfied > 0){
            satisfiedValue = satisfiedValue / counterSatisfied;
            o = o + 2;
        }

        switch(o){

            case 1: //Si no hay satisfechas
                if ((int)Math.round(rejectedValue) >= staticIdle && (int)Math.round(rejectedValue) <= 7){
                    ins.setMAX_INACTIVE((int)Math.round(rejectedValue-1));
                }else{
                    if ((int)Math.round(rejectedValue) < staticIdle){
                        ins.setMAX_INACTIVE(Math.round(staticIdle -1));
                    }else{
                        ins.setMAX_INACTIVE((int)Math.round(rejectedValue/2));
                    }
                }
                break;

            case 2: // Si no hay rechazadas
                //Si entra dentro de unos márgenes
                if ((int)Math.round(satisfiedValue) >= staticIdle && (int)Math.round(satisfiedValue) <= 7){
                    ins.setMAX_INACTIVE((int)Math.round(satisfiedValue-1));
                }else{
                    if ((int)Math.round(satisfiedValue) < staticIdle){
                        ins.setMAX_INACTIVE(staticIdle);
                    }else{
                        ins.setMAX_INACTIVE((int)Math.round(satisfiedValue)/2);
                    }
                }
                break;

            case 3: // Si existen ambas
                if (counterReject > counterSatisfied){
                    int idealIdle = (int)Math.round((rejectedValue * 0.5 + satisfiedValue  * 2) / 2.0);
                    if (idealIdle >= staticIdle && idealIdle <= 7){
                        ins.setMAX_INACTIVE(idealIdle);
                    }else {
                        if (idealIdle < staticIdle) {
                            ins.setMAX_INACTIVE(staticIdle);
                        } else {
                            ins.setMAX_INACTIVE(Math.round(idealIdle/2));
                        }
                    }
                }else{
                        if ((int)Math.round(satisfiedValue) >= staticIdle && (int)Math.round(satisfiedValue) <= 7){
                            ins.setMAX_INACTIVE((int)Math.round(satisfiedValue+1));
                        }else{
                            if ((int)Math.round(satisfiedValue) < staticIdle){
                                ins.setMAX_INACTIVE(staticIdle);
                            }else{
                                ins.setMAX_INACTIVE((int) Math.round(satisfiedValue/2));
                            }
                        }
                }
                break;
            default: //Si no existe ninguna de las dos
                ins.setMAX_INACTIVE(staticIdle+1);
                break;
        }
    }


}