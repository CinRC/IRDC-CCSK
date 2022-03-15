package me.gmx.parser;

import me.gmx.process.nodes.LabelNode;
import me.gmx.process.process.Process;

public class CCSTransitionException extends RuntimeException{

    public CCSTransitionException(Process p, LabelNode l){
        super(String.format("Could act %s on %s",p.origin(),l.toString()));
    }
    public CCSTransitionException(LabelNode l){
        super(String.format("Could act on %s",l.toString()));
    }

}
