package me.gmx.process.process;

import me.gmx.parser.CCSTransitionException;
import me.gmx.process.nodes.LabelNode;

import java.util.Collection;
import java.util.Collections;

public class ProcessImpl extends Process{
    //Unimplemented
    private Process next;

    public ProcessImpl(String s) {
        super(s);
    }

    @Override
    public boolean canAct(LabelNode label) {
        return false;
    }

    @Override
    public Process act(LabelNode label) {
        return new NullProcess();
    }

    @Override
    public String represent() {
        return String.format("[Process(%s)]", origin);
    }

    @Override
    public Collection<Process> getChildren() {
        return Collections.emptySet();
    }

    @Override
    public Collection<LabelNode> getActionableLabels() {
        return Collections.emptySet();
    }

    @Override
    public String origin(){
        return origin +".0";
    }



}
