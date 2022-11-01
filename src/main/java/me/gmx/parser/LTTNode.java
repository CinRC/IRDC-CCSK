package me.gmx.parser;

import me.gmx.process.nodes.Label;
import me.gmx.process.nodes.LabelKey;
import me.gmx.process.process.Process;
import me.gmx.thread.ProcessContainer;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class LTTNode{

    public HashMap<Label, LTTNode> children;
    public Process p;
    public LTTNode parent;
    public Label reverseKey;
    private int depth;

    public LTTNode(Process p){
        this.p = p;
        children = new HashMap<>();
        this.depth = 0;
    }

    public void setParent(LTTNode n){
        this.parent = n;
        this.depth = n.getDepth() + 1;
    }

    public void addChild(Label l, Process pr) {
        LTTNode node = new LTTNode(pr);
        node.setParent(this);
        node.setDepth(depth + 1);
        children.put(l, node);

    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public void enumerate() {
        ProcessContainer pc = new ProcessContainer(p.clone());
        //For every actionable label in current node,
        for (Label l : pc.getActionableLabels()) {
            if (l instanceof LabelKey)
                continue;
            pc.act(l); //Act on that label and make a new node with that child process (clone)
            LTTNode node = new LTTNode(pc.getProcess().clone());
            node.setParent(this);//Set its parent to this,
            children.put(l, node);//Add to list,
            pc.reverseLastAction();//Then reverse and next label.
        }
        for (LTTNode child : children.values())
            child.enumerate();
    }

    public boolean isLeafNode() {
        return children.isEmpty();
    }


    public String toString() {
        /*StringBuilder sb = new StringBuilder();
        String[] tiers = new String[100];
//        Arrays.fill(tiers,"");
        int maxtier = 0;
        for (Map.Entry<Label,LTTNode> entry: children.entrySet()){
            int dp = entry.getValue().getDepth();
            if (dp > maxtier)
                maxtier = dp;
            String s = "";
            for (int i = 0; i < dp; i++)
                s+="    ";
            tiers[dp] = tiers[dp] + "_" + entry.getValue().p.represent();
        }
        for (int i = 0; i < maxtier; i++)
            sb.append("        ");
            sb.append(p.represent());

        for(int i = 1; i <= maxtier;i++){
            sb.append("\n");
            sb.append(tiers[i].replaceAll("_","    "));
        }
        return sb.toString();*/


        return print(new StringBuilder(500), "", "");
    }


    private String print(StringBuilder buffer, String prefix, String childrenPrefix) {
        buffer.append(prefix);
        buffer.append(p.represent());
        buffer.append('\n');
        for (Iterator<Map.Entry<Label, LTTNode>> it = children.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<Label, LTTNode> entry = it.next();
            LTTNode next = entry.getValue();
            Label l = entry.getKey();
            if (it.hasNext()) {
                next.print(buffer, childrenPrefix + "├─" + l + "─ ", childrenPrefix + "│   ");
            } else {
                next.print(buffer, childrenPrefix + "└─" + l + "─ ", childrenPrefix + "    ");
            }
        }
        return buffer.toString();
    }

}
