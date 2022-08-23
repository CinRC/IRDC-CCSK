package me.gmx.process.nodes;

import me.gmx.RCCS;
import me.gmx.parser.CCSGrammar;
import me.gmx.util.RCCSFlag;

import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;

public abstract class Label extends ProgramNode{

    public CCSGrammar grammar;
    UUID id;
    protected Collection<Label> synchronizeLock;
    public int dupe;
    private String channel;
    protected boolean isComplement;
    protected boolean isRestricted = false;

    public Label(int dupeId, String channel){
        dupe = dupeId;
        this.channel = channel;
    }
    //Instanced initializer block ??
    {
        synchronizeLock = new HashSet<>();
    }

    public UUID getId(){
        return id;
    }

    public void setRestricted(boolean b){
        isRestricted = b;
    }

    public boolean isRestricted(){
        return isRestricted;
    }

    /**
     * Determines whether this process can synchronize via tau transition with the given label
     * @param l the given label
     * @return Returns true if the provided label is not in the sync-lock array
     */
    public boolean canSynchronize(Label l){
        return !synchronizeLock.contains(l);
    }

    /**
     * Adds the given label to a private list that determines which labels cannot be synchronized with
     * via a tau transition
     * @param l the given label
     */
    public void addSynchronizationLock(Label l){
        if (!synchronizeLock.contains(l))
            synchronizeLock.add(l);
    }

    public String toString(){
        String s = "";
        s += isComplement() ? CCSGrammar.COMPLEMENT_SIG.pString : "";
        s += getChannel();
        s += RCCS.config.contains(RCCSFlag.DIFFERENTIATE_LABELS)
                ? String.valueOf(dupe) : "";
        return s;
    }

    public boolean isComplement(){
        return isComplement;
    }

    @Override
    public boolean equals(Object o){
        if (!o.getClass().equals(getClass()))
            return false;
        Label label = (Label) o;
        if (RCCS.config.contains(RCCSFlag.UNIQUE_CHANNELS))
            return label.getId().equals(getId());
        else {
            if (isComplement() != label.isComplement())
                return false;

            String t = getChannel() + dupe;
            String n = label.getChannel() + label.dupe;
            //If dupe of -1, do not check dupe
            if (dupe == -1 || label.dupe == -1)
                return getChannel().equals(label.getChannel());
            return t.equals(n); //Check if label + dupe id are equal // a0 == a0, a1 != a0
        }
    }

    public String getChannel(){ return channel; }

    @Override
    public int hashCode(){
        return 0;
    }



    /**
     * Determines whether the given label is the complement of this, or this is the complement of the given label.
     * @param node node to compare to
     * @return true if the given node is 'synchronizable'
     */
    public boolean isComplementOf(Label node){
        /*if (this instanceof LabelNode) {
            if (node instanceof LabelNode)
                return false;
        }else if (this instanceof ComplementLabelNode) {
            if (node instanceof ComplementLabelNode)
                return false;
        }*/
        return node.getChannel().equals(getChannel())
                && node.isComplement() != isComplement();
    }

    public abstract Label clone();


}
