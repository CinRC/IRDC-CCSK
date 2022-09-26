package me.gmx.process.process;

import me.gmx.RCCS;
import me.gmx.parser.CCSGrammar;
import me.gmx.parser.CCSTransitionException;
import me.gmx.process.nodes.Label;
import me.gmx.process.nodes.LabelKey;
import me.gmx.util.RCCSFlag;
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
    @Override
    public Process actOn(Label label) {
        if (ghostKey == null)//Only need to remember once, theoretically
            setPastLife(clone());

        if (left.canAct(label)) {
            left = left.act(label);
            right.isGhost = true;
            if (ghostKey == null) {
                ghostKey = left.getKey();
            }
        }else if (right.canAct(label)){
            right = right.act(label);
            left.isGhost = true;
            if (ghostKey == null) {
                ghostKey = right.getKey();
            }
        }else throw new CCSTransitionException(this,label);
        return this;
    }

    //Because summation should never hold a key on its own
    @Override
    public boolean hasKey(){
        return ghostKey != null;
    }

    @Override
    public LabelKey getKey(){
        //return ghostKey;
        if (left.isGhost)
            return right.getKey();
        else if (right.isGhost)
            return left.getKey();
        else throw new CCSTransitionException(this, "Attempted to get key when no key exists");
    }

    @Override
    public SummationProcess clone() {
        SummationProcess p = new SummationProcess(left.clone(), right.clone());
        p.setPastLife(previousLife);
        p.setKey(key);
        p.ghostKey = ghostKey;
        p.addRestrictions(restrictions);
        p.addPrefixes(getPrefixes());
        return p;
    }



    @Override
    public Collection<Label> getActionableLabels(){
        Collection<Label> s = getActionableLabelsStrict();

        if (ghostKey != null) { //If we have a ghost key
            if (s.contains(ghostKey)) //If ghost key is on the table
                s.remove(ghostKey);   //Remove ghost key (temporarily)
            if (s.stream().noneMatch(LabelKey.class::isInstance)) //Any more keys in list?
                s.add(ghostKey); //No? ok we can do ghost key
            //Otherwise nope
        }
        return withdrawRestrictions(s);
    }

    @Override
    public String toString(){ return represent(); }

    protected Collection<Label> getActionableLabelsStrict() {
        return getActionableLabelsStrictInternal(true);
    }

    /**
     * Returns list of actionable labels before any restrictions are applied
     * @param lock whether or not to apply sync lock to internal labels
     * @return
     */
    private Collection<Label> getActionableLabelsStrictInternal(boolean lock){
        Collection<Label> s = super.getActionableLabels();

        if (!prefixes.isEmpty()){
            s.add(prefixes.peek());
            return s;
        }


        Collection<Label> l = left.isGhost ? Collections.emptySet()
                : left.getActionableLabels();
        Collection<Label> r = right.isGhost ? Collections.emptySet()
                : right.getActionableLabels();
        if (lock)
            for (Label ll: l)
                for (Label rl : r){
                    ll.addSynchronizationLock(rl);
                    rl.addSynchronizationLock(ll);
                }
        s.addAll(l);
        s.addAll(r);
        return s;
    }

    //Override to remove duplicate key displaying issue
    @Override
    protected String represent(String base){
        base += getRestriction().isEmpty() ? "" : String.format("\\{%s}", SetUtil.csvSet(getRestriction()));
        return base;
    }

    @Override
    public String represent() {
        if (ghostKey != null)
            if (RCCS.config.contains(RCCSFlag.SUMMATION_STYLE_1))
                return represent(String.format(
                        "(%s)%s(%s)"
                        , left == null ? "" : left.represent()
                        , operator.toString()
                        , right == null ? "" : right.represent()
                ));
            else if (RCCS.config.contains(RCCSFlag.SUMMATION_STYLE_3)){
                if (left.isGhost)
                    return represent(String.format(
                            "%s"
                            , right == null ? "" : right.represent()
                    ));
                else if (right.isGhost)
                    return represent(String.format(
                            "%s"
                            , left == null ? "" : left.represent()
                    ));
                }else {
                    if (left.isGhost)
                        return represent(String.format(
                                "%s{%s} %s (%s)"
                                , ghostKey
                                , left == null ? "" : left.represent()
                                , operator.toString()
                                , right == null ? "" : right.represent()
                        ));
                    else if (right.isGhost)
                        return represent(String.format(
                                "(%s) %s %s{%s}"
                                , left == null ? "" : left.represent()
                                , operator.toString()
                                , ghostKey
                                , right == null ? "" : right.represent()
                        ));
                }

        return represent(String.format(
                "(%s)%s(%s)"
                , left == null ? "" : left.represent()
                , operator.toString()
                , right == null ? "" : right.represent()
        ));
    }

}
