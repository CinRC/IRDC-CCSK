package me.gmx.process.process;

import me.gmx.RCCS;
import me.gmx.parser.CCSGrammar;
import me.gmx.parser.CCSTransitionException;
import me.gmx.process.nodes.Label;
import me.gmx.process.nodes.LabelKey;
import me.gmx.process.nodes.TauLabelNode;
import me.gmx.util.RCCSFlag;
import me.gmx.util.SetUtil;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

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
        if (ghostKey == null)//Only need to remember once, theoretically
            setPastLife(clone());
        if (left.canAct(label)) {
            left = left.act(label);
            right.isGhost = true;
            if (ghostKey == null) {
                ghostKey = new LabelKey(label);
            }
        }else if (right.canAct(label)){
            right = right.act(label);
            left.isGhost = true;
            if (ghostKey == null) {
                ghostKey = new LabelKey(label);
            }
        }else throw new CCSTransitionException(this,label);
        return this;
    }

    @Override
    public SummationProcess clone() {
        SummationProcess p = new SummationProcess(left.clone(), right.clone());
        p.setPastLife(previousLife);
        p.setKey(key);
        p.ghostKey = ghostKey;
        p.addRestrictions(restrictions);
        return p;
    }


    @Override
    public boolean canRewind(Label label){
        if (!(label instanceof LabelKey))
            return false;

        LabelKey key = (LabelKey) label;
        if (ghostKey.equals(key)){ //Oh no! We are trying to unlock the ghost :O
            //Do we need to do anything before rewinding?
            //So, as far as I know, whenever a decision is made, it must be in the
            //EXACT same state as it was directly after being made in order to rewind.
            //So, in other words: We can only undo a summation choice if the rewinding
            //is the only reverse action that can be taken

        }
        return true;
    }

    @Override
    public Collection<Label> getActionableLabels(){
        Collection<Label> s = super.getActionableLabels();
        Collection<Label> l = left.isGhost ? Collections.emptySet()
                : left.getActionableLabels();
        Collection<Label> r = right.isGhost ? Collections.emptySet()
                : right.getActionableLabels();
        for (Label ll: l)
            for (Label rl : r){
                ll.addSynchronizationLock(rl);
                rl.addSynchronizationLock(ll);
            }
        s.addAll(l);
        s.addAll(r);

        if (ghostKey != null) { //If we have a ghost key
            if (s.contains(ghostKey)) //If ghost key is on the table
                s.remove(ghostKey);   //Remove ghost key (temporarily)
            if (!s.stream().anyMatch(LabelKey.class::isInstance)) //Any more keys in list?
                s.add(ghostKey); //No? ok we can do ghost key
            //Otherwise nope
        }
        return s;
    }

    @Override
    public String represent() {
        if (ghostKey != null)
            if (RCCS.config.contains(RCCSFlag.SUMMATION_STYLE_1))
                return super.represent(String.format(
                        "(%s)%s(%s)"
                        , left == null ? "" : left.represent()
                        , operator.toString()
                        , right == null ? "" : right.represent()
                ));
            else if (RCCS.config.contains(RCCSFlag.SUMMATION_STYLE_3)){
                if (left.isGhost)
                    return super.represent(String.format(
                            "%s"
                            , right == null ? "" : right.represent()
                    ));
                else if (right.isGhost)
                    return super.represent(String.format(
                            "%s"
                            , left == null ? "" : left.represent()
                    ));
                }else {
                    if (left.isGhost)
                        return super.represent(String.format(
                                "%s{%s} %s (%s)"
                                , ghostKey.origin()
                                , left == null ? "" : left.represent()
                                , operator.toString()
                                , right == null ? "" : right.represent()
                        ));
                    else if (right.isGhost)
                        return super.represent(String.format(
                                "(%s) %s %s{%s}"
                                , left == null ? "" : left.represent()
                                , operator.toString()
                                , ghostKey.origin()
                                , right == null ? "" : right.represent()
                        ));
                }



        return super.represent(String.format(
                "(%s)%s(%s)"
                , left == null ? "" : left.represent()
                , operator.toString()
                , right == null ? "" : right.represent()
        ));
    }

}
