package me.gmx.parser;

import me.gmx.process.ProcessTemplate;
import me.gmx.process.nodes.ActionPrefixProcessFactory;
import me.gmx.process.nodes.LabelNodeFactory;
import me.gmx.process.process.*;

import java.lang.Process;
import java.util.List;
import java.util.Set;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;

public class CCSParser {

    private List<Process> processes;
    public CCSParser(){
    }


    public ProcessTemplate parseLine(String line){
//        StringWalker walker = new StringWalker(line, line.length()-1, StringWalker.Direction.BACKWARDS);
        StringWalker walker = new StringWalker(line);
        /*for (CCSGrammar g : CCSGrammar.values()){

            StringBuilder sb = new StringBuilder();
            Matcher m = g.match(line);
            System.out.println(g.name());
            while (m.find()) {

                m.appendReplacement(sb, "_");
            }
            m.appendTail(sb);
            System.out.println(sb.toString());
            System.out.println();
        }*/
        ProcessTemplate template = new ProcessTemplate();
        while(walker.canWalk()){
            //System.out.println(walker.readMemory());
            walker.walk();
            StringBuilder sb = new StringBuilder();
            for (CCSGrammar g : CCSGrammar.values()) {
                if (g.getClassObject() == null || g == CCSGrammar.OP_ACTIONPREFIX || g == CCSGrammar.LABEL || g == CCSGrammar.PROCESS)
                    continue;
                Matcher m = g.match(walker.readMemory());
                //System.out.println(String.format("Found match: [%s] %s", g.name(), m.group()));
                if (m.find()){
                    if (g == CCSGrammar.ACTIONPREFIX_COMPLETE) {
                        //System.out.println("PArsing " + m.group());
                        template.add(ActionPrefixProcessFactory.parse(m.group()));
                    }else if (g == CCSGrammar.OP_CONCURRENT){
                        template.add(new ConcurrentProcess(null,null));
                    }else if (g == CCSGrammar.OP_SUMMATION){
                        template.add(new SummationProcess(null,null));
                    }
                        //template.add();
                    walker.clearMemory();
                }
            }

        }
        return template;
    }



}
