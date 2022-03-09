package me.gmx.process.nodes;

import me.gmx.parser.CCSGrammar;

public class ComplementLabelNode extends LabelNode{

    public ComplementLabelNode(String s) {
        super(s);
        grammar = CCSGrammar.OUT_LABEL;
    }

}
