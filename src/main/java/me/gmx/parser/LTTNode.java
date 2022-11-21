package me.gmx.parser;

import me.gmx.process.ProcessContainer;
import me.gmx.process.nodes.Label;
import me.gmx.process.nodes.LabelKey;
import me.gmx.process.process.Process;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class LTTNode {

    public HashMap<Label, LTTNode> children;
    public Process p;
    public LTTNode parent;
    private int currentDepth, maxDepth;

    public LTTNode(Process p) {
        this.p = p;
        children = new HashMap<>();
        this.currentDepth = 0;
    }

    public void setParent(LTTNode n) {
        this.parent = n;
        this.currentDepth = n.getCurrentDepth() + 1;
    }

    public void addChild(Label l, Process pr) {
        LTTNode node = new LTTNode(pr);
        node.setParent(this);
        node.setCurrentDepth(currentDepth + 1);
        node.echoDepth(0);
        children.put(l, node);
    }

    public int getCurrentDepth() {
        return currentDepth;
    }

    public void setCurrentDepth(int currentDepth) {
        this.currentDepth = currentDepth;
    }

    /**
     * Function is called on each leaf node. Leaf node will start with depth 0, and
     * recursively call parent nodes with depth+1, until reaching the top. At each step
     * up, the parent node will recalculate its max depth based on given depth.
     * Sort of like a callback function to calculate depth with a complexity of O(n), as opposed
     * to O(n^2)   ((I think))
     *
     * @param depth
     */
    protected void echoDepth(int depth) {
        if (depth > maxDepth)
            maxDepth = depth;
        if (parent != null)
            parent.echoDepth(++depth);
    }

    public int getMaxDepth() {
        return maxDepth;
    }

    /**
     * Enumerate all actionable labels on the parent process, and
     * extend the tree by adding new nodes as children to this node from each label,
     * and recursively call this function on all child nodes
     */
    public void enumerate() {
        ProcessContainer pc = new ProcessContainer(p.clone());
        //For every actionable label in current node,
        for (Label l : pc.getActionableLabels()) {
            if (l instanceof LabelKey)
                continue;
            pc.act(l); //Act on that label and make a new node with that child process (clone)
            Process z = pc.getProcess().clone();
            addChild(l, z);
            pc.reverseLastAction();//Then reverse and next label.
        }
        for (LTTNode child : children.values())
            child.enumerate();
    }

    public boolean isLeafNode() {
        return children.isEmpty();
    }


    public String toString() {
        return print(new StringBuilder(500), "", "");
    }


    //https://stackoverflow.com/questions/4965335/how-to-print-binary-tree-diagram-in-java
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
