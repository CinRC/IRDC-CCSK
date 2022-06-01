package me.gmx.process.nodes;

import me.gmx.RCCS;
import me.gmx.parser.CCSGrammar;

import java.util.UUID;

public abstract class Label extends ProgramNode{

    public CCSGrammar grammar;
    UUID id;

    public UUID getId(){
        return id;
    }

    @Override
    public String origin() {
        return origin;
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
        return node.origin().equals(String.format("'%s", this.origin()))
                || origin().equals(String.format("'%s", node.origin()));
    }

    public abstract Object clone();


}
