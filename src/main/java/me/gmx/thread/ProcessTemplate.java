package me.gmx.thread;

import me.gmx.parser.CCSGrammar;
import me.gmx.parser.CCSParserException;
import me.gmx.parser.CCSTransitionException;
import me.gmx.process.nodes.Label;
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

    public void addRestrictionToLastProcess(Collection<Label> restrictions){
        this.tList.getLast().addRestrictions(restrictions);
    }

    public String prettyString(){
        StringBuilder sb = new StringBuilder();
        for (Process p : tList)
            sb.append(p.represent());
        return sb.toString();
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
