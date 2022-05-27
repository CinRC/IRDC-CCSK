package me.gmx.process.nodes;

import me.gmx.parser.CCSGrammar;

import java.util.UUID;

public class LabelNode extends ProgramNode {

    public CCSGrammar grammar;
    private UUID id;
    public LabelNode(String s) {
        origin = s;
        grammar = CCSGrammar.LABEL;
        this.id = UUID.randomUUID();
    }

    @Override
    public String origin() {
        return origin;
    }

    public String toString(){
        return origin();
    }

    public UUID getId(){
        return id;
    }

    public boolean equals(Object o){
        return (o instanceof LabelNode)
                && (((LabelNode)o).getId().equals(getId()));
    }

    //Basically, check if given node is '[this] or [this] is '[given node]
    //TODO: fix
    public boolean isComplementOf(LabelNode node){
        return node.origin().equals(String.format("\'%s", this.origin()))
                || origin().equals(String.format("\'%s", node.origin()));
    }


}
