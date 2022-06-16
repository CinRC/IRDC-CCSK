package me.gmx.process.nodes;

import me.gmx.parser.CCSGrammar;

import java.util.UUID;

public class LabelNode extends Label {

    public CCSGrammar grammar;
    public LabelNode(String s) {
        super(NodeIDGenerator.nextAvailable(), s);
        origin = s;
        grammar = CCSGrammar.LABEL;
        this.id = UUID.randomUUID();
    }

    public LabelNode(LabelNode node){
        super(node.dupe, node.getChannel());
        origin = node.origin();
        grammar = CCSGrammar.LABEL;
        id = node.getId();
    }

    //Basically, check if given node is '[this] or [this] is '[given node]
    //TODO: fix
    @Override
    public LabelNode clone(){
        return new LabelNode(this);
    }

    @Override
    public String origin(){
        return getChannel()+dupe;
    }

}
