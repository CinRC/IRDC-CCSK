package me.gmx.process.process;

import me.gmx.parser.CCSTransitionException;
import me.gmx.process.nodes.Label;

import java.util.*;

public class ActionPrefixProcess extends Process {

    private LinkedList<Label> prefixes;
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
        this.prefixes = new LinkedList<Label>();
        this.prefixes.add(label);
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
        this.prefixes = new LinkedList<Label>();
        this.prefixes.addAll(labels);
    }

    /**
     * Clones process
     * @return Deep clone (hopefully) of this process
     */
    //Do clones need to remember their past lives? I am actually not sure. Seems like an ethical question
    @Override
    protected Process clone() {
        LinkedList<Label> prf = new LinkedList<>();
        prf.addAll(prefixes);
        ActionPrefixProcess p = new ActionPrefixProcess(getProcess().clone(), prf);
        if (previousLife != null) {
            p.setPastLife(previousLife.clone());
            p.key = key.clone();
        }

        return p;
    }

    protected Label getPrefix(){
        return prefixes.get(0);
    }

    protected Process getProcess(){
        return process;
    }

    @Override
    public Process actOn(Label label) {
        List<Label> l = new ArrayList<>();
        //Add every label except the first (because that's the one we act on)
        if (prefixes.get(0).equals(label))
            for (int i = 1; i < prefixes.size(); i++)
                //Clone here?
                l.add((Label) prefixes.get(i));
            else throw new CCSTransitionException(this, label);

        return new ActionPrefixProcess(getProcess(), l);
    }

    @Override
    public String represent() {
        return super.represent(origin());
    }

    /**
     * Gets child processes
     * @return ActionPrefixProcess will always return only a single child process
     */
    @Override
    public Collection<Process> getChildren() {
        return Collections.singleton(process);
    }

    /**
     * Get actionable labels for given process. This process will only ever return a single prefix,
     * so we do not need to worry about tau nodes
     * @return Set of actionable labels
     */
    @Override
    public Collection<Label> getActionableLabels(){
        Collection<Label> s = super.getActionableLabels();
        if (!prefixes.isEmpty())
            s.add(prefixes.get(0));
        return s;
    }

    public String origin(){
        return origin;
    }


}
