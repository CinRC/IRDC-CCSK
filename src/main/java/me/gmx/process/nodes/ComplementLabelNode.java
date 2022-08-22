package me.gmx.process.nodes;

import me.gmx.parser.CCSGrammar;

import java.util.UUID;

/**
 * The "output" portion of a communication channel
 */
public class ComplementLabelNode extends Label{

    public ComplementLabelNode(String s) {
        //Remove the ' when applying channel
        super(NodeIDGenerator.nextAvailable(), s.replaceFirst("'",""));
        grammar = CCSGrammar.OUT_LABEL;
        isComplement = true;
        this.id = UUID.randomUUID();
    }

    private ComplementLabelNode(ComplementLabelNode node){
        super(node.dupe, node.getChannel());
        grammar = CCSGrammar.OUT_LABEL;
        id = node.getId();
    }

    @Override
    public ComplementLabelNode clone(){
        return new ComplementLabelNode(this);
    }


}
