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

    public boolean canSynchronize(Label l){
        return !synchronizeLock.contains(l);
    }

    public void addSynchronizationLock(Label l){
        if (!synchronizeLock.contains(l))
            synchronizeLock.add(l);
    }

    public String toString(){
        return origin();
    }

    public boolean equals(Object o){
        if (RCCS.UNIQUE_CHANNELS)
            return (o instanceof Label)
                    && ((Label)o).getId().equals(getId());
        else {
            return (o instanceof Label)
                    && ((Label) o).origin().equals(origin());
        }
    }

    public boolean isComplementOf(Label node){
        return node.origin().equals(String.format("'%s", origin()))
                || origin().equals(String.format("'%s", node.origin()));
    }

    public abstract Label clone();


}
