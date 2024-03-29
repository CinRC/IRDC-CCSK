package org.cinrc.process.nodes;

import java.util.UUID;
import org.cinrc.parser.CCSGrammar;

public class LabelNode extends Label {



  public LabelNode(String s) {
    super(NodeIDGenerator.nextAvailable(), s);
    isComplement = false;
    grammar = CCSGrammar.LABEL_IN;
    this.id = UUID.randomUUID();
  }

  public LabelNode(LabelNode node) {
    super(node.dupe, node.getChannel());
    grammar = CCSGrammar.LABEL_IN;
    id = node.getId();
    setRestricted(node.isRestricted);

  }

  @Override
  public LabelNode clone() {
    return new LabelNode(this);
  }


}
