package me.gmx.process.nodes;

import me.gmx.parser.CCSGrammar;
import me.gmx.parser.CCSParserException;

import java.util.regex.Matcher;

public class LabelFactory {

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
}
