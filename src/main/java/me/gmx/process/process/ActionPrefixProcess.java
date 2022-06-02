package me.gmx.process.process;

import me.gmx.RCCS;
import me.gmx.parser.CCSTransitionException;
import me.gmx.process.nodes.Label;
import me.gmx.process.nodes.LabelKey;
import me.gmx.process.nodes.LabelNode;
import me.gmx.util.SetUtil;

import java.util.*;

public class ActionPrefixProcess extends Process {

    private LinkedList<Label> prefix;
    private Process process;


    //a.b.c.P

    // prefix c, process P
    // prefix b, process ^
    // prefix a, process ^
    /**
     * Prefixes are nested, so instead of having a single
     * prefix me.gmx.process with a list of prefixes, they will
     * recursively nest eachother as prefix(prefix(prefix(P),c),b),a)
     * @param p Underlying process to be accessed via label
     * @param label Label used to access underlying process
     * a.b.c.P --> c.P, b.(c.P), a.(b.(c.P))
     */


    public ActionPrefixProcess(Process p, Label label){
        this.origin = (label.origin() + "." + p.origin());
        this.process = p;
        this.prefix = new LinkedList<Label>();
        this.prefix.add(label);
    }

    public ActionPrefixProcess(Process p, List<Label> labels){
        String s = "";
        for (Label label : labels){
            s += label.origin();
            s += ".";
        }
        s += p.represent();
        this.origin = s;
        this.process = p;
        this.prefix = new LinkedList<Label>();
        this.prefix.addAll(labels);
    }

/*    *//**
     * Gross code, but works for now. Alternatively, just reparse from origin()
     * @return
     *//*
    @Override
    protected Process clone() {
        ArrayList<Label> prefixes = new ArrayList<>();
        Process p = process;
        while (p instanceof ActionPrefixProcess){
            prefixes.add(((ActionPrefixProcess)p).getPrefix());
            p = ((ActionPrefixProcess) p).getProcess();
        }
        Collections.reverse(prefixes);
        Process pr = p.clone();
        for (Label l : prefixes)
            pr = new ActionPrefixProcess(pr,l);

        return pr;
    }*/

    protected Label getPrefix(){
        return prefix.get(0);
    }

    protected Process getProcess(){
        return process;
    }

    @Override
    public Process actOn(Label label) {
        List<Label> l = new ArrayList<>();
        //Add every label except the first (because that's the one we act on)
        if (prefix.get(0).equals(label))
            for (int i = 1; i < prefix.size(); i++)
                //Clone here?
                l.add((Label) prefix.get(i));
            else throw new CCSTransitionException(this, label);

        return new ActionPrefixProcess(getProcess(), l);
    }

    @Override
    public String represent() {
        return super.represent(origin());
    }

    @Override
    public Collection<Process> getChildren() {
        return Collections.singleton(process);
    }

    @Override
    public Collection<Label> getActionableLabels(){
        Collection<Label> s = super.getActionableLabels();
        if (!prefix.isEmpty())
            s.add(prefix.get(0));
        return s;
    }

    public String origin(){
        return origin;
    }


}
