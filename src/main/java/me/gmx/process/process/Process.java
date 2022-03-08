package me.gmx.process.process;

import me.gmx.process.nodes.LabelNode;

import java.util.Collection;

public interface Process{


    boolean canAct(LabelNode label);

    Process act(LabelNode label);

    String represent();

    Collection<LabelNode> getActionableLabels();

    String origin();

}
