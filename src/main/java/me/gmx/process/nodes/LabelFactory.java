package me.gmx.process.nodes;

import me.gmx.parser.CCSGrammar;
import me.gmx.parser.CCSParserException;

import java.util.regex.Matcher;

public class LabelFactory {

    /**
     * Simply matches the first node, ignores everything else
     *
     * @param s String to parse
     * @return Label parsed from the given strings
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

        throw new CCSParserException(String.format("Could not parse %s into labels", s));
    }

    /**
     * Creates a 'debug' label. Debug labels are created for testing. Their dupeId
     * is set to -1, which is not normally possible. All debug labels will evaluate to be
     * equal to another label if both labels are along the same channel, regardless of dupe
     *
     * @param channel
     * @return
     */
    public static Label createDebugLabel(String channel) {
        Label c = parseNode(channel);
        c.dupe = -1;
        return c;
    }

    public static LabelKey createDebugLabelKey(Label label) {
        LabelKey c = new LabelKey(label);
        c.dupe = -1;
        return c;
    }

    public static Label createLabel(String channel, int dupe){
        Label c = parseNode(channel);
        c.dupe = dupe;
        return c;
    }
}
