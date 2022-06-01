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

    public Process(String s, Collection<Label> restrictions, Process previousLife, LabelKey key){
        origin = s;
        this.restrictions.addAll(restrictions);
        this.previousLife = previousLife;
        this.key = key;
    }

    //TODO: There has to be a better way to do this than reparsing. I really
    //tried to find another way, but shallow fakes are overwhelming and I
    //'already' have the code for this.
    protected Process clone(){
        return CCSParser.parseLine(origin()).export();
    }

    /**
     * Determines whether process can act on given label, without actually acting on it.
     * @param label label to act on
     * @return True if given label is able to be acted on, false otherwise
     */
    public boolean canAct(Label label){
        RCCS.log(String.format("Checking if %s can act on %s",represent(),label.origin()));
        RCCS.log(SetUtil.csvSet(getActionableLabels()));
        return restrictions.contains(label) ? false
                : getActionableLabels().contains(label);
    }

    protected abstract Process actOn(Label label);

    public Process act(Label label){
        if (label instanceof LabelKey)
            return previousLife;

        rememberLife(label);
        return this.actOn(label);
    }

    protected void rememberLife(Label label){
        this.key = new LabelKey(label);
        this.previousLife = this.clone();
    }

    @Deprecated
    public boolean canReverse(LabelKey key){
        return hasKey() && getKey().equals(key);
    }

    /**
     * Reverses the last action performed on this process
     * @return Process after reversal
     */
    @Deprecated
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
        s += hasKey() ? String.format("%s%s"
                , getKey().origin()
                , base)
                : String.format("%s",base);
/*        s += !restrictions.isEmpty() ? String.format("\\{Restriction: %s}"
                , SetUtil.csvSet(restrictions)) : "";*/
        return s;

    }

    public abstract Collection<Process> getChildren();

    public Collection<Label> getActionableLabels(){
        Set<Label> l = new HashSet<>();
        if (RCCS.KEYS_AS_LABELS && hasKey())
            l.add(getKey());
        return l;
    }

    public String origin(){
        return origin;
    }


}
