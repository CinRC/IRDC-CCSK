package me.gmx.process;

import me.gmx.process.process.ProgramNode;

import java.util.LinkedList;

public class ProcessTemplate {
    private LinkedList<? extends ProgramNode> tList;

    public ProcessTemplate(){
        tList = new LinkedList<>();
    }


}
