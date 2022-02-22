package me.gmx.process.nodes;

import me.gmx.parser.CCSGrammar;
import me.gmx.process.process.ProgramNode;

public class LabelNode extends ProgramNode {

    public static CCSGrammar grammar;
    public LabelNode(String s) {
        super(s);
        this.grammar = CCSGrammar.LABEL;
    }
}
