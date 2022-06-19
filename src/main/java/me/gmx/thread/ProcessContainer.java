package me.gmx.thread;

import me.gmx.parser.CCSTransitionException;
import me.gmx.process.nodes.Label;
import me.gmx.process.process.Process;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class ProcessContainer {

    private Process process;
    public ProcessContainer(Process p){
        process = p;
    }
    public String prettyString(){
        return process.represent();
    }

    public Collection<Label> getActionableLabels(){
        return process.getActionableLabels();
    }

    public boolean canAct(Label node){
        return getActionableLabels().contains(node);
    }


    public void act(Label node){
        process.act(node);
    }

}
