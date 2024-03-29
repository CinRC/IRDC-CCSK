package org.cinrc.process.nodes;

import java.util.UUID;
import org.cinrc.parser.CCSGrammar;

/**
 * The "output" portion of a communication channel
 */
public class ComplementLabelNode extends Label {

  public ComplementLabelNode(String channel) {
    //Remove the ' when applying channel
    super(NodeIDGenerator.nextAvailable(), channel);
    grammar = CCSGrammar.LABEL_OUT;
    isComplement = true;
    this.id = UUID.randomUUID();
  }

  private ComplementLabelNode(ComplementLabelNode node) {
    super(node.dupe, node.getChannel());
    grammar = CCSGrammar.LABEL_OUT;
    id = node.getId();
    setRestricted(node.isRestricted);
  }

  @Override
  public ComplementLabelNode clone() {
    return new ComplementLabelNode(this);
  }


}
