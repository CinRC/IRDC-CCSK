package me.gmx.process.process;

import me.gmx.parser.CCSGrammar;
import me.gmx.parser.CCSTransitionException;
import me.gmx.process.nodes.Label;
import me.gmx.process.nodes.TauLabelNode;
import me.gmx.util.SetUtil;

import java.util.Collection;

public class ConcurrentProcess extends ComplexProcess{


    /**
     * @param left - left side me.gmx.process
     * @param right - right side me.gmx.process
     */
    public ConcurrentProcess(Process left, Process right) {
        super(left,right, CCSGrammar.OP_CONCURRENT);
        canHoldLife = false;
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
    public ConcurrentProcess clone() {
        ConcurrentProcess p = new ConcurrentProcess(left.clone(), right.clone());
        p.setPastLife(previousLife.clone());
        p.setKey(key.clone());
        return p;
    }



    /**
     * Returns a set of labels that can be acted on, including tau matches. Theoretically, this is the only
     * process that should be able to support synchronizations.
     * @return Set of labels that can be acted on, including tau matches
     */
    @Override
    public Collection<Label> getActionableLabels(){
        Collection<Label> l = super.getActionableLabels();
        Collection<Label> le = left.getActionableLabels();
        Collection<Label> ri = right.getActionableLabels();
        boolean leftAdded, rightAdded;
        leftAdded = rightAdded = false;

        //If left or right already has tau nodes, dont add them
        if (!le.stream().anyMatch(TauLabelNode.class::isInstance)) {
            l.addAll(le);
            leftAdded = true;
        }
        //If left or right already has tau nodes, dont add them
        if (!ri.stream().anyMatch(TauLabelNode.class::isInstance)) {
            l.addAll(ri);
            rightAdded = true;
        }
        //Check tau nodes in current set
        if (!l.isEmpty())
            l.addAll(SetUtil.getTauMatches(l));

        //If we didn't add for tau matching, add now
        if (!rightAdded)
            l.addAll(ri);
        if (!leftAdded)
            l.addAll(le);


        return l;
    }

}
