package me.gmx.process.process;

import me.gmx.parser.CCSTransitionException;
import me.gmx.process.nodes.Label;

import java.util.Collection;
import java.util.Collections;

public class LabelProcess extends Process{

    private Label label;
    public LabelProcess(Label label){
        this.origin = label.origin();
        this.label = label;
    }

    @Override
    protected Process clone() {
        return new ProcessImpl(origin);
    }

    public Label getLabel(){
        return label;
    }

    @Override
    public boolean canAct(Label label) {
        return getLabel().equals(label);
    }

    @Override
    public Process actOn(Label label) {
        if (!canAct(label))
            return new NullProcess();
        else throw new CCSTransitionException(this, label);
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
