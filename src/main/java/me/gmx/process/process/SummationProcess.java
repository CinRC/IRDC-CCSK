package me.gmx.process.process;

import me.gmx.parser.CCSGrammar;
import me.gmx.parser.CCSTransitionException;
import me.gmx.process.nodes.Label;

public class SummationProcess extends ComplexProcess{

    public SummationProcess(Process left, Process right) {
        super(left,right,CCSGrammar.OP_SUMMATION);
    }

    /**
     * Return only one of the sides, TODO not sure how this should work. Right now it prioritizes the left side
     * @param label Label to act on
     * @return self-process, having acted on label
     */
    @Override
    public Process actOn(Label label) {
        if (left.canAct(label)) {
            return left.act(label);
        }else if (right.canAct(label)){
            return right.act(label);
        }else throw new CCSTransitionException(this,label);

    }


}
