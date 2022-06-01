package me.gmx.process.process;

import me.gmx.parser.CCSGrammar;
import me.gmx.parser.CCSTransitionException;
import me.gmx.process.nodes.Label;

public class ConcurrentProcess extends ComplexProcess{


    /**
     * @param left - left side me.gmx.process
     * @param right - right side me.gmx.process
     */
    public ConcurrentProcess(Process left, Process right) {
        super(left,right, CCSGrammar.OP_CONCURRENT);
    }


    //TODO: Need to clone instead of copy reference, while still carrying over past life
    @Override
    public Process actOn(Label label) {

        if (left.canAct(label))
            left = left.act(label);
        if (right.canAct(label))
            right = right.act(label);
        return this;
/*
        if (left.canAct(label))
            return new ConcurrentProcess(left.act(label), right);
        if (right.canAct(label))
            return new ConcurrentProcess(left, right.act(label));
*/

        //else throw new CCSTransitionException(this, label);
    }

}
