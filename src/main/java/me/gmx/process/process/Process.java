package me.gmx.process.process;

import me.gmx.process.nodes.LabelKey;
import me.gmx.process.nodes.LabelNode;

import java.util.Collection;

public interface Process{

    LabelKey key = null;

    boolean canAct(LabelNode label);

    Process act(LabelNode label);

    String represent();

    Collection<LabelNode> getActionableLabels();

    String origin();

}
