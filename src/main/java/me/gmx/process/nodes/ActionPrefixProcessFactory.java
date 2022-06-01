package me.gmx.process.nodes;

import me.gmx.RCCS;
import me.gmx.parser.CCSGrammar;
import me.gmx.parser.StringWalker;
import me.gmx.process.ProcessTemplate;
import me.gmx.process.process.ActionPrefixProcess;
import me.gmx.process.process.Process;

import java.util.Collections;
import java.util.LinkedList;
import java.util.regex.Matcher;

public class ActionPrefixProcessFactory {


    /**
     * Creates new action prefix process from complete string
     * @param s
     * @return
     */
    public static ActionPrefixProcess parseString(String s){
        ActionPrefixProcess F_c = null;
        //Separate process from labels, assure it is last
        RCCS.log("Creating prefix " + s);
        StringWalker w = new StringWalker(s);
        LinkedList<Label> prefixes = new LinkedList<>();
        Process process;
        do{
            w.walk();
            Matcher m = CCSGrammar.OP_ACTIONPREFIX.match(w.readMemory());
            //Forward through the labels until no more...
            if (m.find()){
                prefixes.add(LabelFactory.parseNode(w.readMemory()));
                w.clearMemory();
            }
            //No more labels, must be a process now or breaks syntax
            process = ProcessFactory.determineRestriction(w.look());
            }while(w.canWalk());

            Collections.reverse(prefixes);
            F_c = new ActionPrefixProcess(process, prefixes);

        return F_c;
    }

    //TODO: parse
}
