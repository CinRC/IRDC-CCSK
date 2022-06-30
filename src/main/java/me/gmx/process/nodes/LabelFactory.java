package me.gmx.process.nodes;

import me.gmx.parser.CCSGrammar;
import me.gmx.parser.CCSParserException;

import java.util.regex.Matcher;

public class LabelFactory {

    /**
     * Simply matches the first node, ignores everything else
     * @param s
     * @return
     */
    public static Label parseNode(String s){
        //TODO: for future reference, can check if start with ', then everything = complement
        Matcher m = CCSGrammar.OUT_LABEL.match(s);
        if (m.find()) {
            return new ComplementLabelNode(m.group());
        }
        m = CCSGrammar.LABEL.match(s);
        if (m.find()) {
            return new LabelNode(m.group());
        }

        throw new CCSParserException(String.format("Could not parse %s into labels",s));
    }

    public static Label createDebugLabel(String channel){
        Label c = parseNode(channel);
        c.dupe = -1;
        return c;
    }
}
