package me.gmx.process.nodes;

import me.gmx.parser.CCSGrammar;
import me.gmx.parser.StringWalker;
import me.gmx.process.ProcessTemplate;
import me.gmx.process.process.ActionPrefixProcess;
import me.gmx.process.process.Process;

import java.util.Collections;
import java.util.LinkedList;
import java.util.regex.Matcher;

public class ActionPrefixProcessFactory {


    public static ActionPrefixProcess parse(String s){
        ActionPrefixProcess F_c = null;
            //Separate process from labels, assure it is last
            System.out.println("Create " + s);
            ProcessTemplate template = new ProcessTemplate();
            StringWalker w = new StringWalker(s);
            LinkedList<LabelNode> prefixes = new LinkedList<>();
            Process process = null;
            do{
                w.walk();
                System.out.println("Walking... Memory: " + w.readMemory());
                Matcher m = CCSGrammar.OP_ACTIONPREFIX.match(w.readMemory());
                //Forward through the labels until no more...
                if (m.find()){
                    System.out.println("Adding " + m.group() + " to prefixes");
                    prefixes.add(LabelNodeFactory.parseNode(w.readMemory()));
                    w.clearMemory();
                }
                //No more labels, must be a process now or breaks syntax
                process = ProcessFactory.determineRestriction(w.look());
            }while(w.canWalk());
            Collections.reverse(prefixes);
            for(LabelNode node : prefixes){
                F_c = new ActionPrefixProcess(process, node);
                process = F_c;
            }

        return F_c;
    }
}
