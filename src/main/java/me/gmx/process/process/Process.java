package me.gmx.process.process;

import me.gmx.RCCS;
import me.gmx.parser.CCSParser;
import me.gmx.process.nodes.Label;
import me.gmx.process.nodes.LabelKey;
import me.gmx.process.nodes.ProgramNode;
import me.gmx.util.SetUtil;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public abstract class Process extends ProgramNode {

    //Storing a key to previous life
    LabelKey key = null;

    //Actual process of previous life
    Process previousLife = null;

    Set<Label> restrictions = new HashSet<>();

    //Is this process capable of remembering a past life
    protected boolean canHoldLife = true;

    public Process(){}

    public void addRestriction(Label... labels){
        for (Label l : labels)
            restrictions.add(l);
    }

    //Very annoying that java cannot process collections as ... streams :(
    public void addRestrictions(Collection<Label> labels){
        for (Label l : labels){
            restrictions.add(l);
        }
    }

    public Collection<Label> getRestriction(){
        return restrictions;
    }

    public Process(Collection<Label> restrictions){
        this.restrictions.addAll(restrictions);
    }

    public Process(Collection<Label> restrictions, Process previousLife, LabelKey key){
        this.restrictions.addAll(restrictions);
        this.previousLife = previousLife;
        this.key = key;
    }

    protected abstract Process clone();

    /**
     * Set past life of this process to the given process
     * Note that it should be good practice to clone a process before giving it to another
     * in the form of a past life.
     * @param p process (hopefully a clone) to set as past life
     */
    protected void setPastLife(Process p){
        this.previousLife = p;
    }

    /**
     * Set CCSK key to provided LabelKey
     * @param key key to set
     */
    protected void setKey(LabelKey key){
        this.key = key;
    }
    /**
     * Determines whether process can act on given label, without actually acting on it.
     * @param label label to act on
     * @return True if given label is able to be acted on, false otherwise
     */
    public boolean canAct(Label label){
        RCCS.log(String.format("Checking if %s can act on %s",represent(),label.origin()));
        RCCS.log(SetUtil.csvSet(getActionableLabels()));
        return getActionableLabels().contains(label);
    }

    /**
     * Internal act method to differentiate different processes behavior when given
     * a certain label to act on
     * @param label label to act on
     * @return process after having been acted on given label
     */
    protected abstract Process actOn(Label label);


    /**
     * Act on a given label. This is the only method that should be called outside
     * of subclasses.
     * @param label label to act on
     * @return will return this, after having acted on the given label
     */
    //Currently, I am unsure if I should leave this as a method that returns a process,
    //Because it usually just returns this;. It may be a good move to change this to
    //a void method. I have not decided which is best. I could also just have act return clone(),
    //but thinking into the future, that would be super inefficient.
    public Process act(Label label){
        if (label instanceof LabelKey && label.equals(getKey()))
            return previousLife;
        else if (canHoldLife)
            rememberLife(label);
        return this.actOn(label);
    }

    /**
     * Saves this process as a clone to memory, and assigns a unique key formed from
     * the given label that links this process to the process in memory. This should not
     * be called by any method other than internal acting methods.
     * @param label label to set as key
     */
    protected void rememberLife(Label label){
        previousLife = clone();
        key = new LabelKey(label);

    }

    /**
     * Whether or not a given process can reverse on a key
     * Deprecated in favor of just checking keys match
     * @param key given key
     * @return true if the process is capable of reversing on the given key
     */
    @Deprecated
    public boolean canReverse(LabelKey key){
        return hasKey() && getKey().equals(key);
    }

    /**
     * Reverses the last action performed on this process
     * Deprecated in favor of just acting on a LabelKey.
     * @return Process after reversal
     */
    @Deprecated
    public Process reverse(){
        return previousLife;
    }

    /**
     * Returns if this process has a CCSK key assigned to it
     * @return true if there is a key assigned to this process
     */
    public boolean hasKey(){
        return this.key != null;
    }

    /**
     * Returns the assigned key
     * @return this process's key, can be null
     */
    public LabelKey getKey(){
        return this.key;
    }

    /**
     * A formatted version of this process to be printed. This is the only method that
     * should be called to print to screen unless you will be comparing processes
     * @return
     */
    public abstract String represent();

    /**
     * Internal represent method to be called by subclasses. This sets the 'format' for
     * all subclasses to take regarding keys and other syntax.
     * @param base Subclass representation
     * @return
     */
    protected String represent(String base){
        String s = "";
        s += hasKey() ? String.format("%s%s"
                , getKey().origin()
                , base)
                : String.format("%s",base);
        s+= getRestriction().isEmpty() ? "" : String.format("\\{%s}",SetUtil.csvSet(getRestriction()));
        return s;
    }

 /*   //TODO: make betterer
    public boolean equals(Object o){
        if (!(o instanceof Process))
            return false;
        Process p = (Process) o;
        if (
                !(p.previousLife.equals(previousLife) || p.getKey().equals(getKey()))
                ||
                (p.origin() != origin())
        )
            return false;

        return true;
    }*/

    public abstract Collection<Process> getChildren();

    /**
     * Base superclass method for getting labels. Only adds any keys
     * @return
     */
    public Collection<Label> getActionableLabels(){
        Set<Label> l = new HashSet<>();
        if (RCCS.KEYS_AS_LABELS && hasKey())
            l.add(getKey());

        return l;
    }

    public abstract String origin();



}
