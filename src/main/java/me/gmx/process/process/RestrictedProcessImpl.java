package me.gmx.process.process;

import me.gmx.process.nodes.Label;
import me.gmx.process.nodes.LabelNode;
import me.gmx.util.SetUtil;

import java.util.*;

/**
 * Deprecated in favor of using base class Process with restrictions collection. No need to
 * have tokenized object for restriction, instead check for restrictions is empty
 */
@Deprecated
public class RestrictedProcessImpl extends ProcessImpl {

    private List<Label> restriction;
    private Process process;


    public RestrictedProcessImpl(Process p, Collection<Label> labels){
        super(String.format("\\{%s}",SetUtil.csvSet(labels)));
        this.process = p;
        restriction = new ArrayList<Label>(labels);
    }

    @Override
    public boolean canAct(Label label) {
        return process.canAct(label) && !restriction.contains(label);
    }

    @Override
    public Process act(Label label) {
        return process.act(label);
    }

    @Override
    public String represent() {
        StringBuilder sb = new StringBuilder();
        for (Label l : restriction)
            sb.append(l.toString()+",");
        //Delete last comma
        sb.deleteCharAt(sb.length()-1);
        return String.format("[Restriction(%s,{%s})",process.represent(),sb.toString());
    }

    @Override
    public Collection<Label> getActionableLabels() {
        Collection z = process.getActionableLabels();
        z.removeAll(restriction);
        return z;
    }

    @Override
    public String origin(){
        return String.format("\\{%s}",SetUtil.csvSet(restriction));
    }
}
