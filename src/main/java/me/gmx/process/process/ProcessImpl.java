package me.gmx.process.process;

import me.gmx.process.nodes.Label;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ProcessImpl extends Process implements ActionableProcess{

    public ProcessImpl(String s) {
        super();
        this.origin = s;
    }

    public ProcessImpl(String s, List<Label> prefixes){
        super();
        this.origin = s;
        this.prefixes.addAll(prefixes);
    }

    @Override
    public Process clone() {
        ProcessImpl p = new ProcessImpl(origin);
        p.setPastLife(previousLife);
        p.addRestrictions(getRestriction());
        p.addPrefixes(getPrefixes());
        return p;
    }

    //TODO: implement
    @Override
    public void execute(){

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
    public Collection<Label> getActionableLabels(){
        return withdrawRestrictions(super.getActionableLabels());
    }

    @Override
    public String origin(){
        return origin;
    }

}
