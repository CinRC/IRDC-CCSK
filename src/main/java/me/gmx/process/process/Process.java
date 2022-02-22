package me.gmx.process.process;

import me.gmx.process.nodes.LabelNode;

public interface Process {


    boolean canAct(LabelNode label);

    Process act(LabelNode label);

    String represent();

}
