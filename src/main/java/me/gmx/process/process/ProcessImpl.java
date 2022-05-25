package me.gmx.process.process;

import me.gmx.parser.CCSTransitionException;
import me.gmx.process.nodes.LabelNode;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

public class ProcessImpl extends Process{
    //Unimplemented
    private Process next;


    public ProcessImpl(String s) {
        super(s);
    }

    public ProcessImpl(String s, Collection<LabelNode> restrictions){
        super(s, restrictions);
    }

    @Override
    public boolean canAct(LabelNode label) {
        return false;
    }

    @Override
    public Process act(LabelNode label) {
        return new NullProcess();
    }

    public String represent() {
        return super.represent(origin());
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
        return origin + ".0";
    }



}
