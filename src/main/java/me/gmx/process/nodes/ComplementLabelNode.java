package me.gmx.process.nodes;

import me.gmx.parser.CCSGrammar;

import java.util.UUID;

/**
 * The "output" portion of a communication channel
 */
public class ComplementLabelNode extends Label{

    public ComplementLabelNode(String s) {
        origin = s;
        grammar = CCSGrammar.OUT_LABEL;
        this.id = UUID.randomUUID();
    }

    public String toString(){
        return this.origin();
    }

    @Override
    public ComplementLabelNode clone(){
        return new ComplementLabelNode(new String(origin));
    }

}
