package org.cinrc.process.process;

import static org.cinrc.util.SetUtil.removeUnsyncableKeys;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.cinrc.parser.CCSParserException;
import org.cinrc.parser.CCSTransitionException;
import org.cinrc.parser.CCSGrammar;
import org.cinrc.process.nodes.Label;
import org.cinrc.process.nodes.LabelKey;
import org.cinrc.process.nodes.TauLabelNode;
import org.cinrc.util.SetUtil;


public class ConcurrentProcess extends ComplexProcess {
  /**
   * @param left  - left side me.gmx.process
   * @param right - right side me.gmx.process
   */

  public ConcurrentProcess(Process left, Process right) {
    super(left, right, CCSGrammar.OP_PAR);
    opString = "|";
  }

  public ConcurrentProcess(Process left, Process right, LinkedList<Label> pfix) {
    super(left, right, CCSGrammar.OP_PAR);
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

  public boolean hasKey() {
    if (key != null) {
      return true;
    }
    if (isPacked()) {
      return left.hasKey() || right.hasKey();
    } else {
      return false;
    }
  }

  public LabelKey getKey() {
    LabelKey k = null;
    Collection<Label> l = getActionableLabelsStrict();
    l.removeIf(x -> !(x instanceof LabelKey)); //remove all non-labelkeys
    if (l.size() == 0) {
      if (key != null) {
        return key;
      } else {
        throw new CCSParserException("Attempted to get key from concurrent process " + represent() +
            " but could not find one!");
      }
    } else if (l.size() == 1) {
      return (LabelKey) l.toArray()[0];
    }
    //If more than one key, find latest
    else if (l.size() > 1) {
      for (Label label : l) { //find which happened most recently
        if (k == null || ((LabelKey) label).time > k.time) {
          k = (LabelKey) label;
        }
      }
    }
    return k;
  }


  private void refactorRecentKey() {
    Collection<Label> l = getActionableLabelsStrict();
    l.removeIf(x -> !(x instanceof LabelKey)); //remove all non-labelkeys
    for (Label label : l) {
      if (key == null || ((LabelKey) label).time > key.time) {
        key = (LabelKey) label;
      }
    }
  }

  @Override
  public ConcurrentProcess clone() {
    ConcurrentProcess p = new ConcurrentProcess(left.clone(), right.clone());
    p.addPrefixes(getPrefixes());
    p.isGhost = isGhost;
    if (previousLife != null) {
      p.setPastLife(previousLife.clone());
    }
    p.setKey(key);
    p.addRestrictions(restrictions);
    return p;
  }

  /**
   * Returns a set of labels that can be acted on, including tau matches. Theoretically, this is the only
   * process that should be able to support synchronizations.
   *
   * @return Set of labels that can be acted on, including tau matches
   */
  @Override
  public Collection<Label> getActionableLabels() {
    Collection<Label> l = super.getActionableLabels();
    if (!prefixes.isEmpty()) {
      return l;
    }

    l.addAll(getActionableLabelsStrict());
    l.addAll(collectSynchronizations(l));
    l = removeRestrictions(l);
    return l;
  }


  protected List<TauLabelNode> collectSynchronizations(Collection<Label> labels){
    List<TauLabelNode> n = new ArrayList<>();
    for (Label l : labels){
      for(Label l2 : labels){
        if (l.canSynchronize(l2) && l2.canSynchronize(l)){

          TauLabelNode node = new TauLabelNode(l, l2);
          if (!n.contains(node)){
            n.add(node);
          }
        }
      }
    }
    return n;
  }


  public Process attemptRewind(LabelKey key) {
    if (getLeftRightLabels().stream().noneMatch(LabelKey.class::isInstance))//no keys left/right?
    {
      if (key.equals(getPrefixKey())) {
        return previousLife;//return previous life
      } else {
        throw new CCSTransitionException(this,
            "Could not rewind on " + key + " because it does not match the prefix!");
      }
    }


    if (left.canAct(key)) {
      left = left.act(key);
    }
    if (right.canAct(key)) {
      right = right.act(key);
    }
    return this;

  }

  protected Collection<Label> getActionableLabelsStrict() {
    Collection<Label> l, r;

    l = left.getActionableLabels();
    r = right.getActionableLabels();
    l.addAll(r);
    return l;
  }


  @Override
  public String toString() {
    return represent();
  }
}
