package me.gmx.process.process;

import me.gmx.RCCS;
import me.gmx.process.nodes.Label;
import me.gmx.process.nodes.LabelKey;
import me.gmx.process.nodes.LabelNode;
import me.gmx.util.SetUtil;

import java.util.*;

public class ActionPrefixProcess extends Process {

    private Label prefix;
    private Process process;


    //a.b.c.P

    // prefix c, process P
    // prefix b, process ^
    // prefix a, process ^
    /**
     * Prefixes are nested, so instead of having a single
     * prefix me.gmx.process with a list of prefixes, they will
     * recursively nest eachother as prefix(prefix(prefix(P),c),b),a)
     * @param process Underlying process to be accessed via label
     * @param label Label used to access underlying process
     * a.b.c.P --> c.P, b.(c.P), a.(b.(c.P))
     */


    public ActionPrefixProcess(Process process, Label label){
        super(label.origin() + "." + process.origin());
        this.process = process;
        this.prefix = label;
    }


    @Override
    public boolean canAct(Label label) {
        return !restrictions.contains(label) && prefix.equals(label);
    }

    @Override
    public Process actOn(Label label) {
        //System.out.println("Acting on " + label.origin() + " (actionprefix)");
        return process;
    }

    @Override
    public String represent() {
        return String.format("[%s.%s]",prefix.origin(),process.represent());
    }

    @Override
    public Collection<Process> getChildren() {
        return Collections.singleton(process);
    }

    @Override
    public Collection<Label> getActionableLabels(){
        return Collections.singleton(this.prefix);
    }


}
