package me.gmx.process.process;

import me.gmx.parser.CCSGrammar;
import me.gmx.process.nodes.LabelNode;

import java.util.*;

public abstract class ComplexProcess extends Process{

    public Process left, right;
    CCSGrammar operator;


    public ComplexProcess(Process left, Process right){
        this.left = left;
        this.right = right;
    }

    @Override
    public Collection<Process> getChildren(){
        return Set.of(left, right);
    }

    public Collection<Process> recurseChildren(){
        Set<Process> s = new HashSet<>();
        s.addAll(getChildren());
        if (left instanceof ComplexProcess)
            s.addAll(((ComplexProcess)left).recurseChildren());
        if (right instanceof ComplexProcess)
            s.addAll(((ComplexProcess)right).recurseChildren());
            return s;
    }

    @Override
    public Collection<LabelNode> getActionableLabels(){
        Set<LabelNode> s = new HashSet<>();
        s.addAll(left.getActionableLabels());
        s.addAll(right.getActionableLabels());
        return s;
    }

    public boolean isInit(){
        return !(left == null || right == null);
    }

    public LinkedList<Process> init(LinkedList<Process> template){
        for (int i = 0; i < template.size(); i++) {

            if (template.get(i) == this) {
                if (left == null) {
                    //System.out.println(String.format("Using %s to init left ", template.get(i - 1).origin()));
                    left = template.remove(i - 1);
                    i--;
                }
                if (right == null) {
                    //System.out.println(String.format("Using %s to init right ", template.get(i + 1).origin()));
                    if (template.size() > i+1)
                        right = template.remove(i + 1);

                }
            }
        }

        //dont think is necessary
        return template;
    }

    @Override
    public String origin(){
        StringBuilder b = new StringBuilder();
        b.append("(");
        if (left == null) b.append(""); else b.append(left.origin());
        b.append(operator);
        if (right == null) b.append(""); else b.append(right.origin());
        b.append(")");
        return b.toString();
    }

}
