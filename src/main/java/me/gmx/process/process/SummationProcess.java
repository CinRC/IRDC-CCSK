package me.gmx.process.process;

import me.gmx.parser.CCSGrammar;
import me.gmx.parser.CCSTransitionException;
import me.gmx.process.nodes.Label;
import me.gmx.process.nodes.LabelKey;
import me.gmx.process.nodes.TauLabelNode;
import me.gmx.util.SetUtil;

import java.util.Collection;
import java.util.Collections;

public class SummationProcess extends ComplexProcess{

    public SummationProcess(Process left, Process right) {
        super(left,right,CCSGrammar.OP_SUMMATION);
    }

    /**
     * Return only one of the sides, TODO not sure how this should work. Right now it prioritizes the left side
     * @param label Label to act on
     * @return self-process, having acted on label
     */

    //Returns left or right, setting their past life to a clone of this
    @Override
    public Process actOn(Label label) {
        Process b = clone();
        if (left.canAct(label)) {
            left.act(label);
            left.setPastLife(b);
            left.setKey(new LabelKey(label));
        }else if (right.canAct(label)){
            right.act(label);
            right.setPastLife(b);
            right.setKey(new LabelKey(label));
        }else throw new CCSTransitionException(this,label);
        return this;
    }

    @Override
    public SummationProcess clone() {
        SummationProcess p = new SummationProcess(left.clone(), right.clone());
        if (hasKey()){
            p.setPastLife(previousLife);
            p.setKey(key);
        }
        p.addRestrictions(restrictions);
        return p;
    }

    @Override
    public Collection<Label> getActionableLabels(){
        Collection<Label> s = super.getActionableLabels();
        Collection<Label> l = left.getActionableLabels();
        Collection<Label> r = right.getActionableLabels();
        //If left is annotated, clear all right labels
        if (l.stream().anyMatch(LabelKey.class::isInstance))
            r.clear();
        if (r.stream().anyMatch(LabelKey.class::isInstance))
            l.clear();
        for (Label ll: l)
            for (Label rl : r){
                ll.addSynchronizationLock(rl);
                rl.addSynchronizationLock(ll);
            }

        s.addAll(l);
        s.addAll(r);
        return s;
    }

}
