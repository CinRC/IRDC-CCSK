package me.gmx.process.process;

import me.gmx.parser.CCSGrammar;
import me.gmx.process.nodes.Label;

public class ConcurrentProcess extends ComplexProcess{


    public static String representString = "|";
    /**
     * @param left - left side me.gmx.process
     * @param right - right side me.gmx.process
     */
    public ConcurrentProcess(Process left, Process right) {
        super(left,right);
        operator = CCSGrammar.OP_CONCURRENT;
    }


    @Override
    public boolean canAct(Label label) {
        return left.canAct(label) || right.canAct(label);
    }

    @Override
    public Process actOn(Label label) {
        if (left.canAct(label))
            left = left.actOn(label);
        if (right.canAct(label))
            right = right.actOn(label);

        return this;
    }

}
