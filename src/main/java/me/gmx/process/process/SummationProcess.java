package me.gmx.process.process;

import me.gmx.parser.CCSGrammar;
import me.gmx.parser.CCSTransitionException;
import me.gmx.process.nodes.Label;
import me.gmx.process.nodes.LabelNode;

public class SummationProcess extends ComplexProcess{

    public SummationProcess(Process left, Process right) {
        super(left,right);
        operator = CCSGrammar.OP_SUMMATION;
    }

    @Override
    public boolean canAct(Label label) {
        return left.canAct(label) || right.canAct(label);
    }

    /**
     * Return only one of the sides, TODO not sure how this should work. Right now it prioritizes the left side
     * @param label Label to act on
     * @return self-process, having acted on label
     */
    @Override
    public Process act(Label label) {
        if (left.canAct(label)) {
            return left.act(label);
        }else if (right.canAct(label)){
            return right.act(label);
        }else throw new CCSTransitionException(this,label);

    }


}
