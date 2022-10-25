package me.gmx.process.process;

import me.gmx.parser.CCSGrammar;
import me.gmx.process.nodes.Label;
import me.gmx.process.nodes.LabelKey;
import me.gmx.util.SetUtil;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class ConcurrentProcess extends ComplexProcess{
    /**
     * @param left - left side me.gmx.process
     * @param right - right side me.gmx.process
     */

    public ConcurrentProcess(Process left, Process right) {
        super(left,right, CCSGrammar.OP_CONCURRENT);
    }

    public ConcurrentProcess(Process left, Process right, LinkedList<Label> pfix) {
        super(left,right, CCSGrammar.OP_CONCURRENT);
        prefixes = pfix;
    }
    //Note: Concurrent processes will never need to hold a key, because data is not destroyed at
    //the complex-process level in this situation.
    @Override
    public Process actOn(Label label) {
        if (left.canAct(label)) {
            left = left.act(label);
        }
        if (right.canAct(label)) {
            right = right.act(label);
        }

        return this;
    }

    public boolean hasKey(){
        return left.hasKey() || right.hasKey();
    }

    public LabelKey getKey(){
        LabelKey key = null;
        Collection<Label> l = getActionableLabelsStrict();
        l.removeIf(x -> !(x instanceof LabelKey));//remove all non-labelkeys
        if (l.size() == 1)
            return (LabelKey) l.toArray()[0]; //If only one key, then this is the key

        for(Label label : l)//otherwise, lets find which one happened last
            if (key == null || ((LabelKey)label).time.isAfter(key.time))
                key = (LabelKey) label;

        return key;
    }

    @Override
    public ConcurrentProcess clone() {
        ConcurrentProcess p = new ConcurrentProcess(left.clone(), right.clone());
        p.addPrefixes(getPrefixes());
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

        Collection l = super.getActionableLabels();
        if (!prefixes.isEmpty()){
            l.add(prefixes.peek());
            return withdrawRestrictions(l);
        }

        l.addAll(getActionableLabelsStrict());
        l.addAll(SetUtil.getTauMatches(l));
        l = withdrawRestrictions(l);
        return SetUtil.removeUnsyncableKeys(this, l);
    }

    protected Collection<Label> getActionableLabelsStrict(){
        Collection<Label> l, r;

        l = left.getActionableLabels();
        r = right.getActionableLabels();
        l.addAll(r);
        return l;
    }


    @Override
    public String toString(){ return represent(); }
}
