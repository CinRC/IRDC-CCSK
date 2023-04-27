package org.cinrc.process.nodes;

import java.util.UUID;

public class TauLabelNode extends Label {

  private final Label a;
  private final Label b;
  public boolean consumeLeft, consumeRight;
  int saveDupe;

  public TauLabelNode(Label node, Label comp) {
    super(-2, node.getChannel());
    this.a = node;
    this.b = comp;
    consumeLeft = consumeRight = false;
    isComplement = false;
    this.saveDupe = NodeIDGenerator.nextAvailableKey();
    this.id = UUID.randomUUID();
}

  public TauLabelNode(String channel){
    super(-2, channel);
    this.a = LabelFactory.createDebugLabelNode(channel);
    this.b = LabelFactory.createDebugComplementLabelNode(channel);
    consumeLeft = consumeRight = false;
    isComplement = false;
    this.saveDupe = NodeIDGenerator.nextAvailableKey();
    this.id = UUID.randomUUID();
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof TauLabelNode node)) {
      return false;
    }
    return node.getChannel().equals(getChannel());
  }

  @Override
  public String toString() {
    return "Tau{"+getChannel()+"}";
    //TODO: modularize

  }

  public String represent(){
    return toString();
  }

  public Label getA() {
    return a;
  }

  public Label getB() {
    return b;
  }

  @Override
  public TauLabelNode clone() {
    return new TauLabelNode(getA().clone(), getB().clone());
  }

  public void destroy() {
    NodeIDGenerator.decrementKey();
  }

  /**
   * From the outside, theoretically tau nodes are equivalent
   *
   * @param l Label to compare
   * @return
   */
  @Override
  public boolean isEquivalent(Label l) {
    return (l instanceof TauLabelNode);
  }

}
