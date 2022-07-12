package me.gmx.process.process;

import me.gmx.parser.CCSGrammar;
import me.gmx.parser.CCSTransitionException;
import me.gmx.process.nodes.Label;
import me.gmx.process.nodes.LabelKey;
import me.gmx.util.SetUtil;

import java.util.*;

public class ConcurrentProcess extends ComplexProcess{
    /**
     * @param left - left side me.gmx.process
     * @param right - right side me.gmx.process
     */
    public ConcurrentProcess(Process left, Process right) {
        super(left,right, CCSGrammar.OP_CONCURRENT);
    }

    private LabelKey recentlyActedKey;
    //Note: Concurrent processes will never need to hold a key, because data is not destroyed at
    //the complex-process level in this situation.
    @Override
    public Process actOn(Label label) {
        if (left.canAct(label)) {
            left = left.act(label);
            recentlyActedKey = left.getKey();
        }
        if (right.canAct(label)) {
            right = right.act(label);
            recentlyActedKey = right.getKey();
        }
        return this;
    }

    @Override
    public Process act(Label label){
        if (!(label instanceof LabelKey))
            return actOn(label);
        //we only want to override reverse handlng
        if (recentlyActedKey == null)
            throw new CCSTransitionException(this, label);
        //if left side key matches, reverse left
        if (recentlyActedKey.equals(left.getKey()))
            left = left.act(recentlyActedKey);
        //if right side key matches, reverse right
        else if (recentlyActedKey.equals(right.getKey()))
            right = right.act(recentlyActedKey);

        else throw new CCSTransitionException(this, label);
        recentlyActedKey = null;
        recompileRecentKey();
        return this;
    }

    private void recompileRecentKey(){
        if (left.hasKey()) {
            if (right.hasKey()) { //Both left & right have keys, compare
                recentlyActedKey = left.getKey().dupe > right.getKey().dupe
                        ? left.getKey() : right.getKey();
            }else
                recentlyActedKey = left.getKey();//only left has key
        }else if (right.hasKey()){//only right has key
            recentlyActedKey = right.getKey();
        }
    }

    @Override
    public boolean hasKey(){
        return recentlyActedKey != null;
    }

    @Override
    public LabelKey getKey(){
        return recentlyActedKey;
    }

    @Override
    public ConcurrentProcess clone() {
        ConcurrentProcess p = new ConcurrentProcess(left.clone(), right.clone());
        p.isGhost = isGhost;
        p.setPastLife(previousLife);
        p.setKey(key);
        p.addRestrictions(restrictions);
        return p;
    }

    /**
     * Returns a set of labels that can be acted on, including tau matches. Theoretically, this is the only
     * process that should be able to support synchronizations.
     * @return Set of labels that can be acted on, including tau matches
     */
    @Override
    public Collection<Label> getActionableLabels(){
        Collection<Label> l = getActionableLabelsStrict();
        l.addAll(SetUtil.getTauMatches(l));
        return withdrawRestrictions(l);
    }

    protected Collection<Label> getActionableLabelsStrict(){
        Collection<Label> l = super.getActionableLabels();
        l.addAll(left.getActionableLabels());
        l.addAll(right.getActionableLabels());
        return l;
    }

}
