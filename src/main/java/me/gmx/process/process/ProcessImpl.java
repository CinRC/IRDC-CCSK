package me.gmx.process.process;

import me.gmx.process.nodes.Label;

import java.util.Collection;
import java.util.Collections;

public class ProcessImpl extends Process{

    public ProcessImpl(String s) {
        this.origin = s;
    }

    @Override
    protected Process clone() {
        return new ProcessImpl(new String(origin));
    }

    @Override
    public boolean canAct(Label label) {
        return false;
    }

    @Override
    public Process actOn(Label label) {
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
    public Collection<Label> getActionableLabels() {
        return super.getActionableLabels();
    }

    @Override
    public String origin(){
        return origin;
    }



}
