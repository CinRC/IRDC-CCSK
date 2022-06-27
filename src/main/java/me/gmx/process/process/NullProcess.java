package me.gmx.process.process;

import me.gmx.process.nodes.Label;
import java.util.Collection;
import java.util.Collections;

public class NullProcess extends Process{

    public NullProcess() {
        super();
    }

    @Override
    public Process clone(){
        return new NullProcess();
    }

    @Override
    public boolean canAct(Label label) {
        return false;
    }

    @Override
    public Process actOn(Label label) {
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

    //Not needed?
    @Override
    public Collection<Label> getActionableLabels() {
        Collection<Label> l = getActionableLabelsStrict();
        l.removeAll(getRestriction());
        return l;
    }

    @Override
    protected Collection<Label> getActionableLabelsStrict() {
        return super.getActionableLabels();
    }

    @Override
    public String origin(){
        return "0";
    }
}
