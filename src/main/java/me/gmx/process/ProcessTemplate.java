package me.gmx.process;

import me.gmx.parser.CCSGrammar;
import me.gmx.parser.CCSTransitionException;
import me.gmx.process.nodes.LabelNode;
import me.gmx.process.process.ComplexProcess;
import me.gmx.process.process.Process;
import me.gmx.process.process.ProgramNode;

import java.util.*;

public class ProcessTemplate {
    private LinkedList<Process> tList;
    public ProcessTemplate(){

        tList = new LinkedList<>();

    }



    public void add(Process node){
        tList.add(node);
    }

    public void initComplex(){
        List<ComplexProcess> complex = new ArrayList<>();
        //Add all complex processes to a list
        for (Process value : tList) {
            if (value instanceof ComplexProcess)
                complex.add((ComplexProcess) value);
        }
        //Iterate through ccs syntax, determining highest structure
        for (CCSGrammar g : CCSGrammar.values()){
            for (ComplexProcess process : complex){
                if (process.getClass() == g.getClassObject()){
                    tList = process.init(tList);
                }
            }
        }
        /*for (int i = 0; i < tList.size();i++) {
            if (tList.get(i) instanceof ComplexProcess){
                System.out.println("instance of complex");
                ComplexProcess p = (ComplexProcess) tList.get(i);
                if (!p.isInit()){
                    System.out.println("test");

                    tList = p.init(tList);
                }
            }
        }*/
    }

    public void write(){
        for (Process o : tList)
            /*if (o instanceof Process){
                System.out.println(String.format("Node: %s", ((Process) o).origin()));
            }
            else System.out.println(o.toString());*/
            System.out.println(o.represent());
        System.out.println();
    }

    public String prettyString(){
        StringBuilder sb = new StringBuilder();
        for (Process o : tList)
            sb.append(o.origin());
        return sb.toString();
    }

    public Set<LabelNode> getActionableLabels(){
        Set<LabelNode> nodes = new HashSet<>();
        for(Process p : tList)
            for (LabelNode n : p.getActionableLabels())
                nodes.add(n);

        return nodes;
    }

    public boolean canAct(LabelNode node){
        return getActionableLabels().contains(node);
    }

    public ProcessTemplate actOn(LabelNode node){
        /*for (Process p : tList){
            if (p.canAct(node)){

                p = p.act(node);
                return true;
            }
        }*/
        for(int i = 0; i < tList.size();i++){
            Process p = tList.get(i);
            if (p.canAct(node)){
                tList.set(i,p.act(node));
                return this;
            }
        }
        throw new CCSTransitionException(node);
        //return false;
    }


}
