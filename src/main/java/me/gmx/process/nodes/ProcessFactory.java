package me.gmx.process.nodes;

import me.gmx.parser.CCSGrammar;
import me.gmx.parser.CCSParserException;
import me.gmx.process.process.Process;
import me.gmx.process.process.RestrictedProcess;
import me.gmx.process.process.UnrestrictedProcess;

import java.util.Set;
import java.util.regex.Matcher;

public class ProcessFactory {

    public static Process determineRestriction(String s){
        Matcher m = CCSGrammar.RESTRICTION.match(s);
        Set<LabelNode> re = null;
        if (m.find()){
            re = Set.of(LabelNodeFactory.parseNode(m.group()));
            while(m.find()){
                re.add(LabelNodeFactory.parseNode(m.group()));
            }
        }

        m = CCSGrammar.PROCESS.match(s);
        if (m.find()) {
            if (re == null)
                return new UnrestrictedProcess(m.group());
            else
                return new RestrictedProcess(new UnrestrictedProcess(m.group()),re);
        }

        throw new CCSParserException("Could not parse any processes from " + s);
    }
}
