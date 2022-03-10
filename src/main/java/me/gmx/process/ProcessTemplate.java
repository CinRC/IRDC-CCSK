package me.gmx.process;

import me.gmx.parser.CCSGrammar;
import me.gmx.parser.CCSTransitionException;
import me.gmx.process.nodes.LabelNode;
import me.gmx.process.process.ComplexProcess;
import me.gmx.process.process.Process;

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
        //Collect complex processes
        Set<ComplexProcess> complex = new HashSet<>();
        for (Process process : tList)
            if (process instanceof ComplexProcess)
                complex.add((ComplexProcess) process);


        for (CCSGrammar g : CCSGrammar.values()){
            for (ComplexProcess p : complex){
                if (p.getClass() == g.getClassObject()){
                    if (p.left == null)
                        //Subsume object to the left
                        p.left = tList.remove(tList.indexOf(p)-1);

                    if (p.right == null)
                        //Subsume object to the right
                        p.right = tList.remove(tList.indexOf(p)+1);

                }
            }
        }
        /*for (CCSGrammar g : CCSGrammar.values()){
            for (int i = 0; i < tList.size(); i++) {
                Process p = tList.get(i);
                if (p.getClass() == g.getClassObject()) {
                    if (p.left == null) {
                        //System.out.println(String.format("Using %s to init left ", tList.get(i - 1).origin()));
                        left = tList.remove(i - 1);
                        i--;
                    }
                    if (right == null) {
                        //System.out.println(String.format("Using %s to init right ", tList.get(i + 1).origin()));
                        if (tList.size() > i+1)
                            right = tList.remove(i + 1);

                    }
                }
            }
        }*/
    }

    public void write(){
        for (Process o : tList)
            System.out.println(o.origin());
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
        for(int i = 0; i < tList.size();i++){
            Process p = tList.get(i);
            if (p.canAct(node)){
                tList.set(i,p.act(node));
                return this;
            }
        }
        throw new CCSTransitionException(node);
    }

    public LinkedList<Process> getProcesses(){
        return this.tList;
    }

    public void prependTemplate(ProcessTemplate t){
        t.getProcesses().addAll(tList);
    }

    public void appendTemplate(ProcessTemplate t){
        tList.addAll(t.getProcesses());
    }


}
