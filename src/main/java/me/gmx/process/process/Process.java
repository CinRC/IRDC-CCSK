package me.gmx.process.process;

import me.gmx.process.nodes.LabelKey;
import me.gmx.process.nodes.LabelNode;
import me.gmx.util.SetUtil;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public abstract class Process extends ProgramNode{

    LabelKey key = null;

    Set<LabelNode> restrictions = new HashSet<>();

    public Process(String s) {
        super(s);
    }

    public Process(){
        super("0");
    }

    public Process(String s, Collection<LabelNode> restrictions){
        super(s);
        this.restrictions.addAll(restrictions);
    }

    public boolean canAct(LabelNode label){
        Collection<LabelNode> n = restrictions;
        n.removeAll(restrictions);
        return n.contains(label);
    }

    public abstract Process act(LabelNode label);

    abstract String represent();

    public abstract Collection<Process> getChildren();

    public abstract Collection<LabelNode> getActionableLabels();

    public String origin(){
        if (!restrictions.isEmpty())
            return origin + "\\{" + SetUtil.csvSet(restrictions) + "}";
        else return origin;
    }


}
