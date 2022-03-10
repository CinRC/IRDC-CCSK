package me.gmx.process.process;

import me.gmx.process.nodes.LabelNode;
import me.gmx.process.nodes.LabelNodeFactory;
import me.gmx.util.SetUtil;

import java.util.*;

/**
 * A restricted me.gmx.process. Cannot act over a set of labels.
 * Form: P\a, a.b.P\{a,b}
 */
public class RestrictedProcessImpl extends ProcessImpl {

    private List<LabelNode> restriction;
    private Process process;


    public RestrictedProcessImpl(Process p, Collection<LabelNode> labels){
        super(String.format("\\{%s}",SetUtil.csvSet(labels)));
        this.process = p;
        restriction = new ArrayList<>(labels);
    }

    @Override
    public boolean canAct(LabelNode label) {
        return process.canAct(label) && !restriction.contains(label);
    }

    @Override
    public Process act(LabelNode label) {
        return process.act(label);
    }

    @Override
    public String represent() {
        StringBuilder sb = new StringBuilder();
        for (LabelNode l : restriction)
            sb.append(l.toString()+",");
        //Delete last comma
        sb.deleteCharAt(sb.length()-1);
        return String.format("[Restriction(%s,{%s})",process.represent(),sb.toString());
    }

    @Override
    public Collection<LabelNode> getActionableLabels() {
        Collection z = process.getActionableLabels();
        z.removeAll(restriction);
        return z;
    }

    @Override
    public String origin(){
        return String.format("\\{%s}",SetUtil.csvSet(restriction));
    }
}
