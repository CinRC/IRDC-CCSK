package me.gmx.process.process;

import me.gmx.process.nodes.LabelNode;

public class SummationProcess implements Process{

    private Process left, right;
    public SummationProcess(Process left, Process right) {
        this.left = left;
        this.right = right;

    }

    @Override
    public boolean canAct(LabelNode label) {
        return left.canAct(label) || right.canAct(label);
    }

    /**
     * Return only one of the sides, TODO not sure how this should work. Right now it prioritizes the left side
     * @param label
     * @return
     */
    @Override
    public Process act(LabelNode label) {
        if (left.canAct(label)) {
            left.act(label);
            return left;
        }else if (right.canAct(label)){
            right.act(label);
            return right;
        }else throw new

    }

    @Override
    public String represent() {
        return String.format("[Summation(%s,%s)]",left.toString(),right.toString());
    }
}
