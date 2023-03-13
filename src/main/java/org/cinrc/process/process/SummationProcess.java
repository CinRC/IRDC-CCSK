package org.cinrc.process.process;

import java.util.Collection;
import java.util.Collections;
import org.cinrc.parser.CCSGrammar;
import org.cinrc.parser.CCSParserException;
import org.cinrc.parser.CCSTransitionException;
import org.cinrc.process.nodes.Label;
import org.cinrc.process.nodes.LabelKey;

public class SummationProcess extends ComplexProcess {

  public SummationProcess(Process left, Process right) {
    super(left, right, CCSGrammar.OP_SUMMATION);
  }

  /**
   * Return only one of the sides, TODO not sure how this should work. Right now it prioritizes the left side
   *
   * @param label Label to act on
   * @return self-process, having acted on label
   */
  @Override
  public Process actOn(Label label) {
    if (ghostKey == null)//Only need to remember once, theoretically
    {
      setPastLife(clone());
    }
    try {
      if (left.canAct(label) && !left.isGhost) {
        left = left.act(label);
        right.isGhost = true;
        if (ghostKey == null) {
          ghostKey = left.getKey();
        }
      } else if (right.canAct(label) && !right.isGhost) {
        right = right.act(label);
        left.isGhost = true;
        if (ghostKey == null) {
          ghostKey = right.getKey();
        }
      } else {
        throw new CCSTransitionException(this, label);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return this;
  }

  //Because summation should never hold a key on its own
  @Override
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
    if (!isPacked()) {
      return null;
    }
    Collection<Label> l = getLeftRightLabels();

    l.removeIf(x -> !(x instanceof LabelKey));//remove all non-labelkeys
    if (l.size() == 0) {
      if (key != null) {
        return key;//prefix key
      } else {
        throw new CCSParserException(
            "Attempted to get key from concurrent process " + represent() +
                " but could not find one!");
      }
    } else if (l.size() == 1)//if theres only one key, and it matches ghost key
    //If only one key, then this is the key
    {
      return (LabelKey) l.toArray()[0];
    }
    //If more than one key,
    else if (l.size() > 1) {
      for (Label label : l)//otherwise, lets find which one happened last
      {
        if (k == null || ((LabelKey) label).time.isAfter(k.time)) {
          k = (LabelKey) label;
        }
      }
    }
    return k;
  }


  @Override
  public SummationProcess clone() {
    SummationProcess p = new SummationProcess(left.clone(), right.clone());
    if (previousLife != null) {
      p.setPastLife(previousLife.clone());
    }
    p.setKey(key);
    p.isGhost = isGhost;
    p.ghostKey = ghostKey;
    p.addRestrictions(restrictions);
    p.addPrefixes(getPrefixes());
    return p;
  }

  public Process attemptRewind(LabelKey key) {
    if (getLeftRightLabels().stream().noneMatch(LabelKey.class::isInstance) ||
        key.equals(ghostKey))//no keys left/right?
    {
      return previousLife;
    }

    if (left.isGhost) {
      right = right.act(key);
      if (!right.hasKey()) {
        right.isGhost = false;
      }
    } else if (right.isGhost) {
      left = left.act(key);
    } else {
      throw new CCSTransitionException(this,
          "Could not rewind on " + key + " because neither side of this process is ghosted.");
    }

    return this;
  }

  @Override
  public Collection<Label> getActionableLabels() {
    Collection<Label> s = getActionableLabelsStrict();

    if (ghostKey != null) { //If we have a ghost key
      //If ghost key is on the table
      s.remove(ghostKey);   //Remove ghost key (temporarily)
      if (s.stream().noneMatch(l -> (l instanceof LabelKey &&
          !l.equals(getPrefixKey())))) //Any more keys in list? (not counting prefix)
      {
        s.add(ghostKey); //No? ok we can do ghost key
      }
      //Otherwise nope
    }
    return withdrawRestrictions(s);
  }

  @Override
  public String toString() {
    return represent();
  }

  protected Collection<Label> getActionableLabelsStrict() {
    return getActionableLabelsStrictInternal(true);
  }

  /**
   * Returns list of actionable labels before any restrictions are applied
   *
   * @param lock whether or not to apply sync lock to internal labels
   * @return List of labels before considering summation Order of Op and restrictions
   */
  private Collection<Label> getActionableLabelsStrictInternal(boolean lock) {
    Collection<Label> s = super.getActionableLabels();

    if (!prefixes.isEmpty()) {
      s.add(prefixes.peek());
      return s;
    }


    Collection<Label> l = left.isGhost ? Collections.emptySet()
        : left.getActionableLabels();
    Collection<Label> r = right.isGhost ? Collections.emptySet()
        : right.getActionableLabels();
    if (lock) {
      for (Label ll : l) {
        for (Label rl : r) {
          ll.addSynchronizationLock(rl);
          rl.addSynchronizationLock(ll);
        }
      }
    }
    s.addAll(l);
    s.addAll(r);
    return s;
  }


}
