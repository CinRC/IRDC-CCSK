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
        this.canHoldLife = false;
    }


    //TODO: Need to clone instead of copy reference, while still carrying over past life
    @Override
    public Process actOn(Label label) {
        if (left.canAct(label))
            left = left.act(label);
        if (right.canAct(label))
            right = right.act(label);
        return this;
    }

    @Override
    public ConcurrentProcess clone(){
        return new ConcurrentProcess(left.clone(), right.clone());
    }

}
