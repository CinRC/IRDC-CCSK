package me.gmx.process.process;

import me.gmx.parser.CCSGrammar;
import me.gmx.process.nodes.LabelNode;

/**
 * This is one of the many ways to deal with things like a.b.(a+b).
 * When introducing parenthesis, this comes up where . can appear
 * in places other than between two nodes, and instead must be treated
 * as a complex process
 */

public class SequentialProcess extends ComplexProcess{
    public SequentialProcess(Process left, Process right) {
        super(left, right);
        operator = CCSGrammar.OP_SEQUENTIAL;
    }

    @Override
    public boolean canAct(LabelNode label) {
        return false;
    }

    @Override
    public Process act(LabelNode label) {
        return null;
    }

    @Override
    public String represent() {
        return null;
    }
}
