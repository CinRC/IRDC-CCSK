package process.nodes;

import parser.CCSGrammar;
import process.ProgramNode;

public class LabelNode extends ProgramNode {

    public CCSGrammar grammar;
    public LabelNode(String s) {
        super(s);
        this.grammar = CCSGrammar.LABEL;
    }
}
