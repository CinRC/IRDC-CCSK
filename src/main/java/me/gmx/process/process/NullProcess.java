package me.gmx.process.process;

import me.gmx.RCCS;
import me.gmx.process.nodes.Label;
import me.gmx.util.RCCSFlag;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class NullProcess extends Process{

    public NullProcess() {
        super();
    }

    public NullProcess(List<Label> prefixes){
        super();
        this.prefixes.addAll(prefixes);
    }

    @Override
    public Process clone(){
        NullProcess p = new NullProcess();
        p.setPastLife(previousLife);
        p.addRestrictions(getRestriction());
        p.addPrefixes(getPrefixes());
        return p;
    }

    @Override
    public Process actOn(Label label) {
        return this;
    }

    @Override
    public String represent() {
        return super.represent(RCCS.config.contains(RCCSFlag.DISPLAY_NULL) ? "0" : "");
    }

    @Override
    public Collection<Process> getChildren() {
        return Collections.emptySet();
    }

    protected Collection<Label> getActionableLabelsStrict() {
        return super.getActionableLabels();
    }

    @Override
    public String origin(){
        return "0";
    }
}
