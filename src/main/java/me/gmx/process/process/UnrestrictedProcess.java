package me.gmx.process.process;

import me.gmx.parser.CCSTransitionException;
import me.gmx.process.nodes.LabelNode;

import java.util.Collection;
import java.util.Collections;

public class UnrestrictedProcess implements Process{

    private Process process;

    private String origin;

    public UnrestrictedProcess(String s) {
        this.origin = s;
        this.process = this;
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
        return String.format("[Process(%s)]", this.origin);
    }

    @Override
    public Collection<LabelNode> getActionableLabels() {
        return Collections.emptySet();
    }

    @Override
    public String origin(){
        return origin;
    }

}
