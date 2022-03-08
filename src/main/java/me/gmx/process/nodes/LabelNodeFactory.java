package me.gmx.process.nodes;

import me.gmx.parser.CCSGrammar;
import me.gmx.parser.CCSParserException;

import java.util.regex.Matcher;

public class LabelNodeFactory {

    public static LabelNode parseNode(String s){
        Matcher m = CCSGrammar.OUT_LABEL.match(s);
        if (m.find()) {
            //System.out.println("Parsing out node " + s);
            return new ComplementLabelNode(m.group());
        }
        m = CCSGrammar.LABEL.match(s);
        if (m.find()) {
            //System.out.println("Parsing node " + s);
            return new LabelNode(m.group());
        }

        throw new CCSParserException(String.format("Could not parse %s into labels",s));


    }
}
