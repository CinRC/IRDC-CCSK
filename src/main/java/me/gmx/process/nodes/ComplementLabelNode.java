package me.gmx.process.nodes;

import me.gmx.parser.CCSGrammar;

/**
 * The "output" portion of a communication channel
 */
public class ComplementLabelNode extends Label{

    public ComplementLabelNode(String s) {
        origin = s;
        grammar = CCSGrammar.OUT_LABEL;
    }

    public String toString(){
        return this.origin();
    }


}
