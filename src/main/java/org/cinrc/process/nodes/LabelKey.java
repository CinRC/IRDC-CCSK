package org.cinrc.process.nodes;

import java.time.Instant;
import java.util.UUID;
import org.cinrc.IRDC;
import org.cinrc.util.RCCSFlag;

public class LabelKey extends Label {

  public Label from;

  public Instant time;

  public LabelKey(Label node) {
    super(node.dupe, node.getChannel());
    this.id = UUID.randomUUID();
    isComplement = false;
    this.from = node;
    if (node instanceof TauLabelNode) {
      this.dupe = ((TauLabelNode) node).saveDupe;
    } else {
      this.dupe = NodeIDGenerator.nextAvailableKey();
    }
    time = Instant.now();

  }

  /**
   * To replicate UUIDs for tau transitions
   *
   * @param key LabelKEy to copy from
   */
  public LabelKey(LabelKey key) {
    super(key.dupe, key.getChannel());
    id = key.getId();
    from = key.from.clone();
    this.dupe = key.dupe;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof LabelKey key)) {
      return false;
    }
    //return key.getId().equals(getId()); //defaults to id
    //return (key.getChannel().equals(getChannel()) && (key.dupe == dupe ? true : key.dupe == -1));
    return key.from.equals(from) && (key.dupe == dupe || key.dupe == -1 || dupe == -1);
  }


  @Override
  public String toString() {
    if (IRDC.config.contains(RCCSFlag.KEYS_MATCH_LABELS)) {
      return String.format("[%s]", from);
    } else {
      return String.format("[k%s]", dupe);
    }
  }

  public LabelKey clone() {
    return new LabelKey(this);
  }
}
