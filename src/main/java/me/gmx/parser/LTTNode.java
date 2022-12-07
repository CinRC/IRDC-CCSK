package me.gmx.parser;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import me.gmx.process.ProcessContainer;
import me.gmx.process.nodes.Label;
import me.gmx.process.nodes.LabelKey;
import me.gmx.process.process.Process;

public class LTTNode {

    public HashMap<Label, LTTNode> children;
    public Process p;
    public Collection<LTTNode> parents;
    private int currentDepth, maxDepth;

    public LTTNode(Process p) {
        this.p = p;
        children = new HashMap<>();
        currentDepth = 0;
        parents = new HashSet<>();
    }

    public HashMap<Label, LTTNode> getOutgoingEdges() {
        return (HashMap<Label, LTTNode>) children.clone();
    }

    public void setParent(LTTNode n) {
        this.parents.add(n);
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
        if (depth > maxDepth) {
            maxDepth = depth;
        }
        if (!parents.isEmpty()) {
            for (LTTNode n : parents) {
                n.echoDepth(++depth);
            }
            //This would be a much more elegant solution, but scope doesnt allow
            //parents.forEach(p -> p.echoDepth(++depth));
        }
    }

    public int getMaxDepth() {
        return maxDepth;
    }

    /**
     * Enumerate all actionable labels on the parent process, and
     * extend the tree by adding new nodes as children to this node from each label,
     * and recursively call this function on all child nodes if recurse is true.
     *
     * @param recurse
     */
    public void enumerate(boolean recurse) {
        ProcessContainer pc = new ProcessContainer(p.clone());
        //For every actionable label in current node,
        for (Label l : pc.getActionableLabels()) {
            if (!(l instanceof LabelKey)) {
                System.out.println("Before acting: " + pc.prettyString());
                pc.act(l); //Act on that label and make a new node with that child process (clone)
                System.out.println("After acting: " + pc.prettyString());
                Process z = pc.getProcess().clone();
                addChild(l, z);
                pc.reverseLastAction();//Then reverse and next label.
                System.out.println("After reversal: " + pc.prettyString());

            } else {
                /*Label originalLabel = ((LabelKey) l).from;
                pc.act(l);
                for (LTTNode n : parents)
                //TODO: Check pc.p already exists in parents, else add it.
                {
                    continue;
                }
                if (originalLabel instanceof TauLabelNode) {
                    ((TauLabelNode) originalLabel).consumeLeft = false;
                    ((TauLabelNode) originalLabel).consumeRight = false;
                }
                pc.act(originalLabel);*/
            }
        }
        if (recurse) {
            for (LTTNode child : children.values()) {
                child.enumerate(true);
            }
        }
    }

    /**
     * Ensures that the given node's edges are a subset of this node's edges.
     * This operation does not guarantee the reverse.
     *
     * @param node
     * @return True if this node this can simulate all edges of given node
     */
    public boolean canSimulate(LTTNode node) {
        Collection<Label> keySet = children.keySet();

        for (Label l : node.children.keySet()) { //for every edge of compared
            int match = 0;
            for (Label l2 : keySet) { //iterate through our edges
                if (l2.isEquivalent(l)) //can our edge do what compared edge can?
                {
                    if (!children.get(l2).canSimulate(node.children.get(l))) {
                        return false;
                    }
                    match = 1;
                    continue;
                }
            }
            if (match == 0) {
                return false;
            }
        }
        return true;
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
        buffer.append(p.represent() + " --- " + this.currentDepth);
        buffer.append('\n');
        for (Iterator<Map.Entry<Label, LTTNode>> it = children.entrySet().iterator();
             it.hasNext(); ) {
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
