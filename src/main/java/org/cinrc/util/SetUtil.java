package org.cinrc.util;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.cinrc.process.nodes.ComplementLabelNode;
import org.cinrc.process.nodes.Label;
import org.cinrc.process.nodes.LabelKey;
import org.cinrc.process.nodes.LabelNode;
import org.cinrc.process.nodes.TauLabelNode;
import org.cinrc.process.process.ComplexProcess;
import org.cinrc.process.process.Process;

public class SetUtil {


  public static String csvSet(Collection<Label> set) {
    if (set.isEmpty()) {
      return "";
    }
    StringBuilder sb = new StringBuilder();
    for (Label o : set) {
      sb.append(o);
      sb.append(",");
    }
    sb.deleteCharAt(sb.length() - 1);
    return sb.toString();
  }

  /**
   * Returns a hashset of TauLabelNodes representing matching complements of labels provided in the given collection
   *
   * @param nodes Collection of Labels to find tau matches from
   * @return Set of TauLabelNode that apply to the given collection
   */
  public static Collection<TauLabelNode> getTauMatches(Collection<Label> nodes) {
    Set<TauLabelNode> tau = new HashSet<>();
    for (Label node : nodes) {
      if (node instanceof ComplementLabelNode) {
        //Cool, there is a complement in the set. Let's see if any matches
        for (Label innerNode : nodes) {
          if (innerNode != node &&
              !(innerNode instanceof LabelKey || innerNode instanceof TauLabelNode)) {
            if (node.isComplementOf(innerNode)) {
              if (node.canSynchronize(innerNode) && innerNode.canSynchronize(node)) {
                //Cool, we found a complement, let's add it to our map.
                TauLabelNode n = new TauLabelNode(node, innerNode); //Don't want duplicates
                if (tau.contains(n) || nodes.contains(n)) {
                  n.destroy();
                } else {
                  tau.add(n);
                }
              }
            }
          }
        }
      }
    }
    return tau;
  }

  //Not sure why, but this no longer is feasible.
  @Deprecated
  public static Collection<Label> removeUnsyncableKeys(ComplexProcess p, Collection<Label> labels) {
    Iterator<Label> iter = labels.iterator();
    while (iter.hasNext()) {
      Label l = iter.next();
      if (!(l instanceof LabelKey key))//we only care about keys
      {
        continue;
      }
      if (!(key.from instanceof TauLabelNode)) {
        continue; //don't care about regular keys
      }
      try {
        if (!SetUtil.recursiveIsSyncable(p, key))//both sides need to be able to do it
        {
          iter.remove();
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return labels;
  }

  /**
   * Checks if the two given sets of labels contain the same labels, without accounting for dupe. In essence,
   * this is like the base HashSet#equals function, but instead of calling Label#equals, it calls Label#simulates
   *
   * @param labels
   * @param labels2
   * @return
   */
  public static boolean labelsEqual(AbstractSet<Label> labels, AbstractSet<Label> labels2) {
    Collection<Label> copy = new HashSet<>();
    for (Label l : labels) {
      Label l2 = l.clone();
      l2.dupe = -1;
      copy.add(l2);
    }
    return copy.equals(labels2);
  }

  public static boolean recursiveIsSyncable(Process p, LabelKey key) {
    if (!(p instanceof ComplexProcess cp)) {
      return false;
    }

    if (cp.left.canAct(key) && cp.right.canAct(key)) {
      return true;
    } else {
      return (recursiveIsSyncable(cp.left, key)
          || recursiveIsSyncable(cp.right, key));
    }
  }

  public static boolean containsOrTau(Collection<Label> labels, Label l) {
    if (l instanceof TauLabelNode tau) {
      boolean b = ((labels.contains(tau.getA())) || (labels.contains(tau.getB())));

      return b;
    } else {
      return labels.contains(l);
    }
  }

  /**
   * Returns true if given label is affected by restriction syntax. In this situation,
   * only labelnodes and complement labelnodes should be restrictable. This leaves
   * keys and taus outside of restriction.
   *
   * @param l label
   * @return bool
   */
  public static boolean isRestrictable(Label l) {
    return (l instanceof LabelNode || l instanceof ComplementLabelNode);
  }

}