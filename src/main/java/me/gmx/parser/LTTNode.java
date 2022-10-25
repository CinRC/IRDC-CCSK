package me.gmx.parser;

import me.gmx.process.nodes.Label;
import me.gmx.process.nodes.LabelKey;
import me.gmx.process.process.Process;
import me.gmx.thread.ProcessContainer;

import java.util.HashMap;

public class LTTNode{

    public HashMap<Label, LTTNode> children;
    public Process p;
    public LTTNode parent;
    public Label reverseKey;

    public LTTNode(Process p){
        this.p = p;
        children = new HashMap<>();
    }

    public void setParent(LTTNode n){
        this.parent = n;
    }

    public void addChild(Label l, Process pr){
        LTTNode node = new LTTNode(pr);
        node.setParent(this);
        children.put(l,node);
    }

    public void enumerate(){
        ProcessContainer pc = new ProcessContainer(p.clone());
        //For every actionable label in current node,
        for (Label l : pc.getActionableLabels()){
            pc.act(l); //Act on that label and make a new node with that child process (clone)
            LTTNode node = new LTTNode(pc.getProcess().clone());
            node.setParent(this);//Set its parent to this,
            children.put(l,node);//Add to list,
            pc.reverseLastAction();//Then reverse and next label.
        }
    }

    public boolean isLeafNode(){
        return children.isEmpty();
    }

}
