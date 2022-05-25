package me.gmx.process;

import me.gmx.RCCS;
import me.gmx.parser.CCSGrammar;
import me.gmx.parser.CCSParserException;
import me.gmx.parser.CCSTransitionException;
import me.gmx.process.nodes.LabelNode;
import me.gmx.process.process.ComplexProcess;
import me.gmx.process.process.Process;

import java.util.*;



public class ProcessTemplate {
    private final LinkedList<Process> tList;
    public ProcessTemplate(){
        tList = new LinkedList<>();
    }
    public boolean isInit = false;


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
        isInit = true;
    }


    @Deprecated
    public void write(){
        for (Process o : tList)
            System.out.println(o.origin());
        System.out.println();
    }

    public String prettyString(){
        StringBuilder sb = new StringBuilder();
        for (Process o : tList) {
            sb.append(o.origin());
        }
        return sb.toString();
    }

    public Set<LabelNode> getActionableLabels(){
        Set<LabelNode> nodes = new HashSet<>();
        for(Process p : tList)
            nodes.addAll(p.getActionableLabels());
        return nodes;
    }

    /**
     * Exports ProcessTemplate as a process. ProcessTemplate should always init down to
     * a single 'parent' process.
     * @return Parent process
     * @throws CCSParserException If for some reason there are more than 1 parent processes
     */
    public Process export(){
        if (!isInit)
            initComplex();
        if (tList.size() != 1)
            throw new CCSParserException("Could not export process template into process!");
        else return tList.get(0);
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
