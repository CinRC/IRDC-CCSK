package me.gmx.process.nodes;

import me.gmx.parser.CCSGrammar;

/**
 * The "output" portion of a communication channel
 */
public class ComplementLabelNode extends LabelNode{

    public ComplementLabelNode(String s) {
        super(s);
        grammar = CCSGrammar.OUT_LABEL;
    }

    public String toString(){
        return this.origin();
    }

    //TODO fix
    public boolean equals(Object o){
        return (o instanceof LabelNode) && (((LabelNode)o).origin().equals(this.origin()));
    }

}
