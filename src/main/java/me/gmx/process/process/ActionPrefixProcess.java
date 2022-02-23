package me.gmx.process.process;

import me.gmx.parser.CCSParserException;
import me.gmx.process.nodes.LabelNode;

import java.util.regex.Matcher;

public class ActionPrefixProcess implements Process{

    private LabelNode prefix;
    private Process process;

    /**
     * Prefixes are nested, so instead of having a single
     * prefix me.gmx.process with a list of prefixes, they will
     * recursively nest eachother as prefix(prefix(prefix(P),c),b),a)
     * @param s a.b.c.P --> c.P, b.(c.P), a.(b.(c.P))
     */
    public ActionPrefixProcess(String s) {
        Matcher match = LabelNode.grammar.match(s);
        if (match.find())
            prefix = new LabelNode(match.group());
        else throw new CCSParserException();
    }


    @Override
    public boolean canAct(LabelNode label) {
        return prefix == label;
    }

    @Override
    public Process act(LabelNode label) {
        return process;
    }

    @Override
    public String represent() {
        return String.format("[%s.%s]",prefix,process.represent());
    }
}
