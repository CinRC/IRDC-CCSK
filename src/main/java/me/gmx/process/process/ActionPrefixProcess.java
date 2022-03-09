package me.gmx.process.process;

import me.gmx.parser.CCSGrammar;
import me.gmx.parser.CCSParser;
import me.gmx.parser.CCSParserException;
import me.gmx.parser.StringWalker;
import me.gmx.process.ProcessTemplate;
import me.gmx.process.nodes.LabelNode;
import me.gmx.process.nodes.LabelNodeFactory;

import java.util.*;
import java.util.regex.Matcher;

public class ActionPrefixProcess implements Process{

    private LabelNode prefix;
    private Process process;


    //a.b.c.P

    // prefix c, process P
    // prefix b, process ^
    // prefix a, process ^
    /**
     * Prefixes are nested, so instead of having a single
     * prefix me.gmx.process with a list of prefixes, they will
     * recursively nest eachother as prefix(prefix(prefix(P),c),b),a)
     * @param process Underlying process to be accessed via label
     * @param label Label used to access underlying process
     * a.b.c.P --> c.P, b.(c.P), a.(b.(c.P))
     */


    public ActionPrefixProcess(Process process, LabelNode label){
        this.process = process;
        this.prefix = label;
    }


    /**
     * still deciding how to determine equivalency
     * @param label
     * @return
     */
    @Override
    public boolean canAct(LabelNode label) {
        return prefix.equals(label);
    }

    @Override
    public Process act(LabelNode label) {
        //System.out.println("Acting on " + label.origin() + " (actionprefix)");
        return process;
    }

    @Override
    public String represent() {
        return String.format("[%s.%s]",prefix.origin(),process.represent());
    }

    @Override
    public Collection<LabelNode> getActionableLabels(){
        return Collections.singleton(this.prefix);
    }

    @Override
    public String origin(){
        return prefix.origin() +"."+ process.origin();
    }
}
