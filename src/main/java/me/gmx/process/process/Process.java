package me.gmx.process.process;

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
    //Note: This is NOT how it's going to work in the end. This is just a test
    Process previousLife = null;

    Set<Label> restrictions = new HashSet<>();

    public Process(String s) {
        origin = s;
    }

    public Process(){
        origin = "0";
    }

    public Process(String s, Collection<Label> restrictions){
        origin = s;
        this.restrictions.addAll(restrictions);
    }

    /**
     * Determines whether process can act on given label, without actually acting on it.
     * @param label label to act on
     * @return True if given label is able to be acted on, false otherwise
     */
    public boolean canAct(Label label){
        return restrictions.contains(label);
    }

    protected abstract Process actOn(Label label);

    public Process act(Label label){
        rememberLife(label);
        return this.actOn(label);
    }

    protected void rememberLife(Label label){
        this.key = new LabelKey(label);
        this.previousLife = this;
    }

    public boolean canReverse(LabelKey key){
        return hasKey() && getKey().equals(key);
    }

    /**
     * Reverses the last action performed on this process
     * @return Process after reversal
     */
    public Process reverse(){
        return previousLife;
    }

    public boolean hasKey(){
        return this.key != null;
    }

    public LabelKey getKey(){
        return this.key;
    }

    public abstract String represent();

    protected String represent(String base){
        String s = "";
        s += hasKey() ? String.format("[Key: %s](%s)"
                , getKey().origin()
                , base)
                : "(%s)";
        s += !restrictions.isEmpty() ? String.format("\\{Restriction: %s}"
                , SetUtil.csvSet(restrictions)) : "";
        return s;

    }

    public abstract Collection<Process> getChildren();

    public abstract Collection<Label> getActionableLabels();

    public String origin(){
        if (!restrictions.isEmpty())
            return origin + "\\{" + SetUtil.csvSet(restrictions) + "}";
        else return origin;
    }


}
