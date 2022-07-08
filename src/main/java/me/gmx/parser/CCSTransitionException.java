package me.gmx.parser;

import me.gmx.process.nodes.Label;
import me.gmx.process.nodes.LabelNode;
import me.gmx.process.process.Process;

public class CCSTransitionException extends RuntimeException{

    public CCSTransitionException(Process p, Label l){
        super(String.format("Could act %s on %s",p.represent(),l.toString()));
    }
    public CCSTransitionException(Label l){
        super(String.format("Could act on %s",l.toString()));
    }

    public CCSTransitionException(Process p, String s){
        super(String.format("Process %s failed to act: %s",p.represent(),s));
    }
}
