package me.gmx.process.process;

import javafx.util.Pair;
import me.gmx.RCCS;
import me.gmx.parser.CCSGrammar;
import me.gmx.parser.CCSTransitionException;
import me.gmx.process.nodes.Label;
import me.gmx.process.nodes.LabelKey;
import me.gmx.process.nodes.ProgramNode;
import me.gmx.process.nodes.TauLabelNode;
import me.gmx.util.RCCSFlag;
import me.gmx.util.SetUtil;
import me.gmx.util.StringUtil;

import java.util.*;

public abstract class Process extends ProgramNode {

    //Storing a key to previous life
    LabelKey key = null;

    //Actual process of previous life
    Process previousLife = null;

    //Passthru key for summation processes
    protected LabelKey ghostKey = null;


    protected boolean isGhost = false;

    protected LinkedList<Label> prefixes = new LinkedList<>();
    Set<Label> restrictions = new HashSet<>();

    public boolean displayKey = !RCCS.config.contains(RCCSFlag.HIDE_KEYS);

    protected boolean canActOnKey = true;
    public Process() {}

    /**
     * Removes restrictions from given process. Because of the way label equality
     * checking works, the removeAll method in collection does not work.
     * @param labels Base set of labels to apply restrictions to
     * @return Collection of labels with restrictions removed
     */
    protected Collection<Label> withdrawRestrictions(Collection<Label> labels){
        for (Label l : labels) {
            for (Label r : getRestriction()) {
                //if (r.isComplement() == l.isComplement())
                if (r.getChannel().equals(l.getChannel()))
                    //iter.remove();
                    l.setRestricted(true);
            }
        }
        return labels;
    }



    public void addRestrictions(Collection<Label> labels){
        restrictions.addAll(labels);
    }

    public Collection<Label> getRestriction(){
        return restrictions;
    }

    public Process(Collection<Label> restrictions){
        this.restrictions.addAll(restrictions);
    }

    @Deprecated
    public Process(Collection<Label> restrictions, Process previousLife, LabelKey key){
        this.restrictions.addAll(restrictions);
        this.previousLife = previousLife;
        this.key = key;
    }

    public abstract Process clone();

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
        Collection<Label> c = getActionableLabels();
        return SetUtil.containsOrTau(c,label) && !label.isRestricted();
    }

    /**
     * Internal act method to differentiate different processes behavior when given
     * a certain label to act on
     * @param label label to act on
     * @return process after having been acted on given label
     */
    protected abstract Process actOn(Label label);

    @Deprecated
    public boolean isAnnotated(){
        return getActionableLabels().stream().anyMatch(LabelKey.class::isInstance);
    }

    /**
     * Act on a given label. This is the only method that should be called outside
     * of subclasses.
     *
     * @param label label to act on
     * @return will return this, after having acted on the given label
     */
    public Process act(Label label) {

        //Key handling
        if (label instanceof LabelKey labelkey) {
            if (ghostKey == null && hasKey()) //Just a regular process? Is it reversible?
                if (getKey().equals(label) && canActOnKey) //Make sure keys match
                    return attemptRewind(labelkey); //Rewind!
            //Ghost key present?
            if (label.equals(ghostKey)) { //Is the key correct?
                return attemptRewind(labelkey); //Okay, rewind!
            } else {
                return this.actOn(label); //Key incorrect? passthru!
            }

        }

        Label l = null;
        if (prefixes.isEmpty())
            //TODO: Throw error?
            return this.actOn(label);
        else l = prefixes.getFirst();

        //Simply check if label == prefix
        if (label.equals(prefixes.getFirst())) {
            actInternal(label);
            return this;
        }
        //Check if tau can eliminate prefixes. if not, continue to pass down
         if (label instanceof TauLabelNode tau){
             if (l.equals(tau.getA()) && !tau.consumeLeft) { //prefix == a and left is free
                tau.consumeLeft = true;
                return actInternal(tau);
            }else if (l.equals(tau.getB()) && !tau.consumeRight){
                tau.consumeRight = true;
                return actInternal(tau);
            }
        }

        return this.actOn(label);
    }

    public Process actInternal(Label l){
        setPastLife(clone());
        getPrefixes().removeFirst();
        setKey(new LabelKey(l));
        return this;
    }


    /**
     *
     * @param labels An ordered list of prefixes, where labels[0] is the leftmost label
     */
    public void addPrefixes(List<Label> labels){
        for (Label l : labels)
            prefixes.addLast(l);
    }

    public LinkedList<Label> getPrefixes(){
        return prefixes;
    }

    public boolean removePrefix(int index){
        try{
            getPrefixes().remove(index);
            return true;
        }catch(IndexOutOfBoundsException e){
            return false;
        }
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
     *
     * @return this process's key, can be null
     */
    public LabelKey getKey() {
        if (key == null) {

            throw new CCSTransitionException(this, "Null key");
        }
        return this.key;
    }

    public abstract Process attemptRewind(LabelKey l);


    /**
     * A formatted version of this process to be printed. This is the only method that
     * should be called to print to screen unless you will be comparing processes
     *
     * @return String to be displayed to user that represents this process
     */
    public String represent() {
        StringBuilder s = new StringBuilder(represent(origin()));
        for (Pair<Label, LabelKey> pair : getLabelKeyPairs()) {
            s.insert(0, pair.getKey().toString()
                    + pair.getValue().toString() + ".");
        }
        return s.toString();
    }

    /**
     * Internal represent method to be called by subclasses. This sets the 'format' for
     * all subclasses to take regarding keys and other syntax.
     * @param base Subclass representation
     * @return Internal string to be called and replaced
     */
    protected String represent(String base){
        StringBuilder s = new StringBuilder();
        //[key]prefix.prefix.base

        for(Pair<Label,LabelKey> pair : getLabelKeyPairs()){
            s.insert(0, pair.getKey().toString()
                    + pair.getValue().toString() + ".");
        }
        s.append(String.format("%s%s"
                , StringUtil.representPrefixes(getPrefixes())
                , base));
        //If sent a null string, remove last dot
        if (base.equals(""))
            s.deleteCharAt(s.length()-1);
        s.append(getRestriction().isEmpty() ? "" : String.format("\\{%s}", SetUtil.csvSet(getRestriction())));
        if (RCCS.config.contains(RCCSFlag.HIDE_PARENTHESIS)) {
            return s.toString().replaceAll(String.format("\\%s", CCSGrammar.OPEN_PARENTHESIS), "")
                    .replaceAll(String.format("\\%s", CCSGrammar.CLOSE_PARENTHESIS), "");
        } else return s.toString();
    }

    public abstract Collection<Process> getChildren();

    /**
     * Base superclass method for getting labels. Only adds any keys
     * @return If hasKey, then key, otherwise empty set
     */
    public Collection<Label> getActionableLabels(){
        Set<Label> l = new HashSet<>();
        if (!prefixes.isEmpty())
            l.add(prefixes.getFirst());
        if (hasKey())//TODO: Fix for concurrent processes whos key points to left or right, duplicating it when added
            l.add(getKey());

        return l;
    }

    public Collection<Process> recurseChildren(){
        Set<Process> s = new HashSet<>(getChildren());
        for (Process p : getChildren())
            s.addAll(p.recurseChildren());
        return s;
    }


    public List<Pair<Label, LabelKey>> getLabelKeyPairs() {
        List<Pair<Label, LabelKey>> l = new ArrayList<Pair<Label, LabelKey>>();
        if (hasPrefixKey()) {
            l.add(new Pair<Label, LabelKey>(getPrefixKey().from, getPrefixKey()));
            //It's necessary to check that previous life key doesnt match current key, because summation processes can have both a ghostkey and a normal key
            if (previousLife.hasPrefixKey() && !previousLife.getPrefixKey().equals(getPrefixKey()))
                l.addAll(previousLife.getLabelKeyPairs());
        }
        return l;
    }

    public boolean hasPrefixKey() {
        return key != null;
    }

    public LabelKey getPrefixKey() {
        return key;
    }

    public abstract String origin();



}
