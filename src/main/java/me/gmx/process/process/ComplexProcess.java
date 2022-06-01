package me.gmx.process.process;

import me.gmx.RCCS;
import me.gmx.parser.CCSGrammar;
import me.gmx.process.nodes.Label;
import me.gmx.process.nodes.LabelKey;
import me.gmx.process.nodes.LabelNode;

import java.util.*;

/**
 * Complex process class is a process that is represented by two processes
 * linked together by an operator
 */
public abstract class ComplexProcess extends Process{

    public Process left, right;
    CCSGrammar operator;


    public ComplexProcess(Process left, Process right, CCSGrammar operator){
        this.left = left;
        this.right = right;
        this.operator = operator;
    }

    /**
     * Returns a non-recursive sub-process
     * @return
     */
    @Override
    public Collection<Process> getChildren(){
        return Set.of(left, right);
    }

    /**
     * Returns a recursive set of sub-processes by calling the same function
     * on all children
     * @return
     */
    public Collection<Process> recurseChildren(){
        Set<Process> s = new HashSet<>();
        s.addAll(getChildren());
        if (left instanceof ComplexProcess)
            s.addAll(((ComplexProcess)left).recurseChildren());
        if (right instanceof ComplexProcess)
            s.addAll(((ComplexProcess)right).recurseChildren());
            return s;
    }

    /**
     *
     * @return Collection of all labels that can be acted upon
     */
    @Override
    public Collection<Label> getActionableLabels(){
        Collection<Label> s = super.getActionableLabels();
        s.addAll(left.getActionableLabels());
        s.addAll(right.getActionableLabels());
        return s;
    }

    /**
     * Checks if the process has been packed
     * @return
     */
    public boolean isPacked(){
        return !(left == null || right == null);
    }

    /**
     * "Packs" processes to the left and right of this process to form a packed complex process.
     * @param template ordered list of processes representing a formula that the process will pack from.
     * @return leftover processes that were not packed
     */
    public LinkedList<Process> pack(LinkedList<Process> template){
        for (int i = 0; i < template.size(); i++) {

            if (template.get(i) == this) {
                if (left == null) {
                    RCCS.log(String.format("Using %s to init left ", template.get(i - 1).origin()));
                    left = template.remove(i - 1);
                    i--;
                }
                if (right == null) {
                    RCCS.log(String.format("Using %s to init right ", template.get(i + 1).origin()));
                    if (template.size() > i+1)
                        right = template.remove(i + 1);

                }
            }
        }

        //dont think is necessary
        return template;
    }

    @Override
    public String represent(){
        return super.represent(String.format(
                "(%s)%s(%s)"
                , left == null ? "" : left.represent()
                , operator.toString()
                , right == null ? "" : right.represent()
        ));
    }

    /**
     * Returns the 'debug' form of human readable representation
     * @return
     */
    @Override
    public String origin(){
        StringBuilder b = new StringBuilder();

        if (left == null) b.append(""); else b.append(left.origin());
        b.append(operator);
        if (right == null) b.append(""); else b.append(right.origin());
        return b.toString();


    }

}
