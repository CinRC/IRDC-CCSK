package me.gmx.process.nodes;

import me.gmx.RCCS;
import me.gmx.parser.CCSGrammar;

import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;

public abstract class Label extends ProgramNode{

    public CCSGrammar grammar;
    UUID id;
    protected Collection<Label> synchronizeLock;
    //Instanced initializer block ??
    {
        synchronizeLock = new HashSet<>();
    }

    public UUID getId(){
        return id;
    }

    @Override
    public String origin() {
        return origin;
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
        return origin();
    }

    @Override
    public boolean equals(Object o){
        if (RCCS.UNIQUE_CHANNELS)
            return (o instanceof Label)
                    && ((Label)o).getId().equals(getId());
        else {
            return (o instanceof Label)
                    && ((Label) o).origin().equals(origin());
        }
    }

    /**
     * Determines whether the given label is the complement of this, or this is the complement of the given label.
     * @param node node to compare to
     * @return true if the given node is 'synchronizable'
     */
    public boolean isComplementOf(Label node){
        return node.origin().equals(String.format("'%s", origin()))
                || origin().equals(String.format("'%s", node.origin()));
    }

    public abstract Label clone();


}
