package me.gmx.process.nodes;

import me.gmx.parser.CCSGrammar;
import me.gmx.process.process.ProgramNode;

public class OperatorNode extends ProgramNode{
    public CCSGrammar grammar;
    public OperatorNode(String s) {
        super(s);
        this.grammar = CCSGrammar.LABEL;
    }
}
