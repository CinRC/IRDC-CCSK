package org.cinrc.process.process;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import javafx.util.Pair;
import org.cinrc.IRDC;
import org.cinrc.parser.CCSTransitionException;
import org.cinrc.process.nodes.Label;
import org.cinrc.process.nodes.LabelKey;
import org.cinrc.process.nodes.TauLabelNode;
import org.cinrc.util.RCCSFlag;

@Deprecated
public class ActionPrefixProcess extends Process {

  private final LinkedList<Label> prefixes;
  private final Process process;

  public ActionPrefixProcess(Process p, List<Label> labels) {
    this.process = p;
    displayKey = false;
    this.prefixes = new LinkedList<Label>();
    this.prefixes.addAll(labels);
    recalculateOrigin();
  }


  /**
   * Clones process
   *
   * @return Deep clone (hopefully) of this process
   */
  //Do clones need to remember their past lives? I am actually not sure. Seems like an ethical question
  @Override
  public ActionPrefixProcess clone() {
    LinkedList<Label> prf = new LinkedList<>();
    prf.addAll(prefixes);
    ActionPrefixProcess p = new ActionPrefixProcess(getProcess().clone(), prf);
    if (hasKey()) {
      p.setPastLife(previousLife);
      p.setKey(getKey());
    }
    p.ghostKey = ghostKey;
    p.isGhost = isGhost;
    p.addRestrictions(getRestriction());

    return p;
  }

  protected Label getPrefix() {
    return prefixes.get(0);
  }

  protected Process getProcess() {
    return process;
  }

  @Override
  public boolean canAct(Label label) {
    IRDC.log(String.format("Checking if %s can act on %s", represent(), label));
    Collection<Label> labels = getActionableLabels();
    if (label instanceof TauLabelNode tau) {
      return labels.contains(tau.getA()) && !tau.consumeLeft ||
          labels.contains(tau.getB()) &&
              !tau.consumeRight; //Basically check if it can either act on left or right, and if it has
      // already been acted on
    } else {
      return labels.contains(label); //super.canAct(label);
    }
  }


  /**
   * Acts on label. sets past life to clone of this
   *
   * @param label label to act on
   * @return Clone of this process
   */
  @Override
  public Process actOn(Label label) {
    if (prefixes.isEmpty()) {
      throw new CCSTransitionException(this, label);
    }

    Process p;
    if (label instanceof TauLabelNode tau) {

      if (getPrefix().equals(tau.getA()) && !tau.consumeLeft) { //prefix == a and left is free
        tau.consumeLeft = true;
        return actInternal(tau);
      } else if (getPrefix().equals(tau.getB()) && !tau.consumeRight) {
        tau.consumeRight = true;
        return actInternal(tau);
      } else {
        throw new CCSTransitionException(this, label);
      }
    }

    if (getPrefix().equals(label)) {
      return actInternal(label);
    } else {
      throw new CCSTransitionException(this, label);
    }

  }

  public Process actInternal(
      Label label) { //to save some lines of code. does not check label equality
    Process p;
    if (prefixes.size() < 2) {
      p = getProcess();
      p.setPastLife(clone());
      p.setKey(new LabelKey(label));
    } else {
      setPastLife(clone());
      setKey(new LabelKey(label));
      p = this;
    }
    prefixes.removeFirst();
    recalculateOrigin();
    return p;
  }

  private void recalculateOrigin() {
    String s = "";
    for (Label label : prefixes) {
      s += String.format("%s.", label);
    }
    //If we don't want to see null processes, then remove last . and dont represent
    if (!IRDC.config.contains(RCCSFlag.DISPLAY_NULL) && getProcess() instanceof NullProcess
        && !prefixes.isEmpty()) {
      s = s.substring(0, s.length() - 1);
    } else {
      s += getProcess().represent();
    }
    this.origin = s;
  }

  @Override
  public String represent() {
    String s = super.represent(origin());
    for (Pair<Label, LabelKey> pair : getLabelKeyPairs()) {
      s = pair.getKey().toString()
          + pair.getValue().toString() + "." + s;
    }
    return s;
  }


  public Process attemptRewind(LabelKey key) {
    return null;
  }

  @Override
  public String toString() {
    return represent();
  }

  /**
   * Gets child processes
   *
   * @return ActionPrefixProcess will always return only a single child process
   */
  @Override
  public Collection<Process> getChildren() {
    return Collections.singleton(process);
  }


  public String origin() {
    return origin;
  }

}
