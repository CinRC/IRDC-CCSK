package org.cinrc.process.nodes;

import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;
import org.cinrc.IRDC;
import org.cinrc.parser.CCSGrammar;
import org.cinrc.util.RCCSFlag;

public abstract class Label extends ProgramNode {

  private final String channel;
  public CCSGrammar grammar;
  public int dupe;
  public boolean isDebugLabel;
  protected Collection<Label> synchronizeLock;
  protected boolean isComplement;
  protected boolean isRestricted = false;
  UUID id;

  //Instanced initializer block ??
  {
    synchronizeLock = new HashSet<>();
  }

  public Label(int dupeId, String channel) {
    dupe = dupeId;
    this.channel = channel;
    isDebugLabel = dupeId == -1;
  }

  public UUID getId() {
    return id;
  }

  public boolean isRestricted() {
    return isRestricted;
  }

  public void setRestricted(boolean b) {
    isRestricted = b;
  }

  /**
   * Determines whether this process can synchronize via tau transition with the given label
   *
   * @param l the given label
   * @return Returns true if the provided label is not in the sync-lock array
   */
  public boolean canSynchronize(Label l) {
    return !synchronizeLock.contains(l);
  }

  /**
   * Adds the given label to a private list that determines which labels cannot be synchronized with
   * via a tau transition
   *
   * @param l the given label
   */
  public void addSynchronizationLock(Label l) {
    if (!synchronizeLock.contains(l)) {
      synchronizeLock.add(l);
    }
  }

  public String toString() {
    String s = "";
    s += isComplement() ? CCSGrammar.COMPLEMENT_SIG.pString : "";
    s += getChannel();
    s += IRDC.config.contains(RCCSFlag.DIFFERENTIATE_LABELS)
        ? String.valueOf(dupe) : "";
    return s;
  }


  public boolean isComplement() {
    return isComplement;
  }

  @Override
  public boolean equals(Object o) {
    if (!o.getClass().equals(getClass())) {
      return false;
    }
    Label label = (Label) o;
//    if (IRDC.config.contains(RCCSFlag.UNIQUE_CHANNELS)) {
//      return label.getId().equals(getId());
//    } else {
    if (isComplement() != label.isComplement()) {
      return false;
    }

    String t = getChannel() + dupe;
    String n = label.getChannel() + label.dupe;
    //If dupe of -1, do not check dupe
    if (dupe == -1 || label.dupe == -1) {
      return getChannel().equals(label.getChannel());
    }

    return t.equals(n); //Check if label + dupe id are equal // a0 == a0, a1 != a0
    //   }
  }

  /**
   * Simply checks if given label's class, channel, and restrictions match this. Used for checking if two labels
   * are the "same", even if they have different IDs.
   *
   * @param l Label to compare
   * @return True if l's class, channel, and restrictions compare equally.
   */
  public boolean isEquivalent(Label l) {
    if (l == null) {
      return false;
    }
    if (l.getClass() != this.getClass()) //probably a better way to do this
    {
      return false;
    }
    return (l.getChannel().equals(getChannel())
        && l.isRestricted == isRestricted());
  }

  public String getChannel() {
    return channel;
  }

  @Override
  public int hashCode() {
    return 0;
  }


  /**
   * Determines whether the given label is the complement of this, or this is the complement of the given label.
   *
   * @param node node to compare to
   * @return true if the given node is 'synchronizable'
   */
  public boolean isComplementOf(Label node) {
    return node.getChannel().equals(getChannel())
        && node.isComplement() != isComplement();
  }

  public abstract Label clone();


}
