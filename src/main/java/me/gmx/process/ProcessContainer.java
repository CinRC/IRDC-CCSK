package me.gmx.process;

import java.util.Collection;
import me.gmx.parser.CCSTransitionException;
import me.gmx.process.nodes.Label;
import me.gmx.process.nodes.LabelKey;
import me.gmx.process.process.Process;

public class ProcessContainer {

    private Process process;

    public ProcessContainer(Process p) {
        process = p;
    }

    public String prettyString() {
        return process.represent();
    }

    public Collection<Label> getActionableLabels() {
        Collection<Label> labels = process.getActionableLabels();
        labels.removeIf(Label::isRestricted);

        return labels;
    }

    public boolean canAct(Label node) {
        Collection<Label> l = getActionableLabels();
        return l.contains(node);
    }

    //TODO
    public boolean canActInto(Process p) {
        for (Label l : getActionableLabels()) {
            Process p2 = getProcess().clone();
            if (p2.act(l).equals(getProcess())) {
                return true;
            }
        }
        return false;
    }

    public void act(Label node) {
        process = process.act(node);
    }

    /**
     * Reverses the last action performed. Simply checks if the process has a key, and if so
     * reverses along that key.
     */
    public void reverseLastAction() {
        try {
            if (process.hasKey()) {
                process = process.act(process.getKey());
            } else {
                throw new CCSTransitionException(process, "Attempted to reverse, but found no key");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public LabelKey getKey() {
        return process.getKey();
    }

    public boolean canReverse() {
        return process.hasKey();
    }

    public void reverseOn(LabelKey key) {
        if (canReverse()) {
            if (process.getKey().equals(key)) {
                process = process.act(key);
            } else {
                throw new CCSTransitionException(process, key);
            }
        }
    }

    public Process getProcess() {
        return process;
    }
}
