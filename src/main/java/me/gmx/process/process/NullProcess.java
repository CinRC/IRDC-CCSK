package me.gmx.process.process;

import me.gmx.process.nodes.Label;
import me.gmx.process.nodes.LabelNode;

import java.util.Collection;
import java.util.Collections;

public class NullProcess extends Process{
    public NullProcess() {
        super();
    }

    @Override
    public boolean canAct(Label label) {
        return false;
    }

    @Override
    public Process act(Label label) {
        return this;
    }

    @Override
    public String represent() {
        return "0";
    }

    @Override
    public Collection<Process> getChildren() {
        return Collections.emptySet();
    }

    @Override
    public Collection<Label> getActionableLabels() {
        return Collections.emptySet();
    }

    @Override
    public String origin(){
        return "0";
    }
}
