package me.gmx.process.process;

import me.gmx.RCCS;
import me.gmx.parser.CCSTransitionException;
import me.gmx.process.nodes.Label;
import me.gmx.process.nodes.LabelKey;

import java.util.*;

public class ActionPrefixProcess extends Process {

    private LinkedList<Label> prefixes;
    private Process process;

    public ActionPrefixProcess(Process p, List<Label> labels){
        this.process = p;
        this.prefixes = new LinkedList<Label>();
        this.prefixes.addAll(labels);
        recalculateOrigin();
    }


    /**
     * Clones process
     * @return Deep clone (hopefully) of this process
     */
    //Do clones need to remember their past lives? I am actually not sure. Seems like an ethical question
    @Override
    protected ActionPrefixProcess clone() {
        LinkedList<Label> prf = new LinkedList<>();
        prf.addAll(prefixes);
        ActionPrefixProcess p = new ActionPrefixProcess(getProcess().clone(), prf);
        if (hasKey()) {
            p.setPastLife(previousLife);
            p.setKey(getKey());
        }
        p.addRestrictions(getRestriction());

        return p;
    }

    protected Label getPrefix(){
        return prefixes.get(0);
    }

    protected Process getProcess(){
        return process;
    }


    /**
     * Acts on label. sets past life to clone of this
     * @param label label to act on
     * @return Clone of this process
     */
    @Override
    public Process actOn(Label label) {
        if (getPrefix().equals(label)) {
            setPastLife(clone());
            prefixes.removeFirst();
            setKey(new LabelKey(label));
        }
        recalculateOrigin();
        return this;
    }

    private void recalculateOrigin(){
        String s = "";
        for (Label label : prefixes)
            s += String.format("%s.",label.origin());
        //If we don't want to see null processes, then remove last . and dont represent
        if (!RCCS.IMPLICIT_NULL_PROCESSES && getProcess() instanceof NullProcess) {
            s = s.substring(0,s.length()-1);
        }else{
            s += getProcess().represent();
        }
        this.origin = s;
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
        s.removeAll(restrictions);
        return s;
    }

    public String origin(){
        return origin;
    }


}
