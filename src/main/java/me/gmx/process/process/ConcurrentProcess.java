package me.gmx.process.process;

import me.gmx.process.nodes.LabelNode;

public class ConcurrentProcess implements Process{

    private Process left, right;

    /**
     * @param left - left side me.gmx.process
     * @param right - right side me.gmx.process
     */
    public ConcurrentProcess(Process left, Process right) {
        this.left = left;
        this.right = right;
    }


    @Override
    public boolean canAct(LabelNode label) {
        return left.canAct(label) || right.canAct(label);
    }

    @Override
    public Process act(LabelNode label) {
        if (left.canAct(label))
            left = left.act(label);
        if (right.canAct(label))
            right = right.act(label);

        return this;
    }

    @Override
    public String represent() {
        return String.format("[Concurrent(%s, %s)]",left.toString(),right.toString());
    }
}
