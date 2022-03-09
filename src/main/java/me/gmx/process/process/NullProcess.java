package me.gmx.process.process;

import me.gmx.process.nodes.LabelNode;

import java.util.Collection;
import java.util.Collections;

public class NullProcess implements Process{
    @Override
    public boolean canAct(LabelNode label) {
        return false;
    }

    @Override
    public Process act(LabelNode label) {
        return this;
    }

    @Override
    public String represent() {
        return "0";
    }

    @Override
    public Collection<LabelNode> getActionableLabels() {
        return Collections.emptySet();
    }

    @Override
    public String origin(){
        return "0";
    }
}
