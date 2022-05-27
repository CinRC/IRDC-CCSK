package me.gmx.process.nodes;

import me.gmx.parser.CCSGrammar;

import java.util.UUID;

public class LabelNode extends Label {

    public CCSGrammar grammar;
    public LabelNode(String s) {
        origin = s;
        grammar = CCSGrammar.LABEL;
        this.id = UUID.randomUUID();
    }

    //Basically, check if given node is '[this] or [this] is '[given node]
    //TODO: fix



}
