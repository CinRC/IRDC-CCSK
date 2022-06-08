package me.gmx.process.nodes;

import me.gmx.parser.CCSGrammar;
import me.gmx.parser.CCSParserException;
import me.gmx.process.process.Process;
import me.gmx.process.process.RestrictedProcessImpl;
import me.gmx.process.process.ProcessImpl;

import java.util.Set;
import java.util.regex.Matcher;

public class ProcessFactory {

    public static Process determineRestriction(String s){
        Matcher m = CCSGrammar.OPEN_RESTRICTION.match(s);
        Set<Label> re = null;
        if (m.find()){
            re = Set.of(LabelFactory.parseNode(m.group()));
            while(m.find()){
                re.add(LabelFactory.parseNode(m.group()));
            }
        }

        m = CCSGrammar.PROCESS.match(s);
        if (m.find()) {
            if (re == null)
                return new ProcessImpl(m.group());
            else
                return new RestrictedProcessImpl(new ProcessImpl(m.group()),re);
        }

        throw new CCSParserException("Could not parse any processes from " + s);
    }
}
