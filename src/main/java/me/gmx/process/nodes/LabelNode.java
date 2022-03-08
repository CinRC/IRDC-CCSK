package me.gmx.process.nodes;

import me.gmx.parser.CCSGrammar;
import me.gmx.process.process.ProgramNode;

public class LabelNode extends ProgramNode {

    public CCSGrammar grammar;
    public LabelNode(String s) {
        super(s);
        grammar = CCSGrammar.LABEL;
    }

    public String toString(){
        return this.origin();
    }

    //TODO fix
    public boolean equals(Object o){
        return (o instanceof LabelNode) && (((LabelNode)o).origin().equals(this.origin()));
    }


}
