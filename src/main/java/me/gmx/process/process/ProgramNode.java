package me.gmx.process.process;

public abstract class ProgramNode {

    protected String origin;

    public ProgramNode(String s){
        this.origin = s;
    }
    public abstract String origin();
}
