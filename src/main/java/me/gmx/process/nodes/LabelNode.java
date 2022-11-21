package me.gmx.process.nodes;

import java.util.UUID;
import me.gmx.parser.CCSGrammar;

public class LabelNode extends Label {

  public CCSGrammar grammar;

  public LabelNode(String s) {
    super(NodeIDGenerator.nextAvailable(), s);
    isComplement = false;
    grammar = CCSGrammar.LABEL;
    this.id = UUID.randomUUID();
  }

  public LabelNode(LabelNode node) {
    super(node.dupe, node.getChannel());
    grammar = CCSGrammar.LABEL;
    id = node.getId();
    setRestricted(node.isRestricted);

  }

  //Basically, check if given node is '[this] or [this] is '[given node]
  //TODO: fix
  @Override
  public LabelNode clone() {
    return new LabelNode(this);
  }


}
