package me.gmx.process.process;

import me.gmx.parser.CCSGrammar;
import me.gmx.parser.CCSTransitionException;
import me.gmx.process.nodes.LabelNode;

public class SummationProcess extends ComplexProcess{


    public static String representString = "+";

    public SummationProcess(Process left, Process right) {
        this.left = left;
        this.right = right;
        operator = CCSGrammar.OP_SUMMATION;
    }

    @Override
    public boolean canAct(LabelNode label) {
        return left.canAct(label) || right.canAct(label);
    }

    /**
     * Return only one of the sides, TODO not sure how this should work. Right now it prioritizes the left side
     * @param label Label to act on
     * @return self-process, having acted on label
     */
    @Override
    public Process act(LabelNode label) {
        if (left.canAct(label)) {
            return left.act(label);
        }else if (right.canAct(label)){
            return right.act(label);
        }else throw new CCSTransitionException(this,label);

    }

    @Override
    public String represent() {
        return String.format("[Summation(%s,%s)]",left.represent(),right.represent());
    }
    //a.b.V + c.a.P | b.Q
    // prefix
}
