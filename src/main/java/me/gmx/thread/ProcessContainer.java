package me.gmx.thread;

import me.gmx.parser.CCSTransitionException;
import me.gmx.process.nodes.Label;
import me.gmx.process.nodes.LabelKey;
import me.gmx.process.process.Process;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
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
        Collection<Label> labels = process.getActionableLabels();
        labels.removeIf(Label::isRestricted);

        return labels;
    }

    public boolean canAct(Label node){
        return getActionableLabels().contains(node);
    }

    public void act(Label node){
        process = process.act(node);
    }
    //TODO: Implement - Timestamps?
    public void reverseLastAction(){
        /*if (process.hasKey())
            process = process.act(process.getKey());
        else throw new CCSTransitionException(process, "Attempted to reverse, but found no key");*/
    }

    public LabelKey getKey(){
        return process.getKey();
    }

    public boolean canReverse(){
        return process.hasKey();
    }

    public void reverseOn(LabelKey key){
        if (canReverse())
            if (process.getKey().equals(key))
                process = process.act(key);
            else throw new CCSTransitionException(process, key);
    }

    public Process getProcess(){
        return process;
    }
}
