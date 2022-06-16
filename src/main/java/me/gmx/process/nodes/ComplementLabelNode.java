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
        this.id = UUID.randomUUID();
        origin = s;
    }

    private ComplementLabelNode(ComplementLabelNode node){
        super(node.dupe, node.getChannel());
        grammar = CCSGrammar.OUT_LABEL;
        id = node.getId();
        origin = node.origin();
    }

    public String toString(){
        return this.origin();
    }

    @Override
    public ComplementLabelNode clone(){
        return new ComplementLabelNode(this);
    }

    public String origin(){
        return String.format("%s%s", CCSGrammar.COMPLEMENT_SIG,super.origin());
    }

}
