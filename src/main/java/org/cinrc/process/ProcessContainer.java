package org.cinrc.process;

import java.util.Collection;
import java.util.Iterator;
import org.cinrc.parser.CCSTransitionException;
import org.cinrc.process.nodes.Label;
import org.cinrc.process.nodes.LabelKey;
import org.cinrc.process.nodes.NodeIDGenerator;
import org.cinrc.process.nodes.TauLabelNode;
import org.cinrc.process.process.ComplexProcess;
import org.cinrc.process.process.Process;
import org.cinrc.util.SetUtil;

public class ProcessContainer {

  private Process process;

  public ProcessContainer(Process p) {
    process = p;
  }

  public String prettyString() {
    return process.represent();
  }

  public Collection<Label> getActionableLabels() {
    Collection<Label> labels = process.getActionableLabels();
    labels = removeUnsyncableKeys(labels);

    return labels;
  }

  public boolean canAct(Label node) {
    Collection<Label> l = getActionableLabels();
    return l.contains(node);
  }

  protected Collection<Label> removeUnsyncableKeys(Collection<Label> labels){
    if (!(process instanceof ComplexProcess cp))
      return labels;
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
        if (!cp.recursiveIsSyncable(key))//both sides need to be able to do it
        {
          iter.remove();
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return labels;
  }


  public void act(Label node) {
    process = process.act(node);
  }

  /**
   * Reverses the last action performed. Simply checks if the process has a key, and if so
   * reverses along that key.
   */
  public void reverseLastAction() {
    try {
      if (process.hasKey()) {
        process = process.act(process.getKey());

      } else {
        throw new CCSTransitionException(process, "Attempted to reverse, but found no key");
      }

    } catch (Exception e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  public LabelKey getKey() {
    return process.getKey();
  }

  public boolean canReverse() {
    return process.hasKey();
  }

  public void reverseOn(LabelKey key) {
    if (canReverse()) {
      if (process.getKey().equals(key)) {
        process = process.act(key);
      } else {
        throw new CCSTransitionException(process, key);
      }
    }
  }

  public Process getProcess() {
    return process;
  }
}
