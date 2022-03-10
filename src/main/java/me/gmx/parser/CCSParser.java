package me.gmx.parser;

import me.gmx.process.ProcessTemplate;
import me.gmx.process.nodes.ActionPrefixProcessFactory;
import me.gmx.process.process.*;

import java.lang.Process;
import java.util.List;
import java.util.regex.Matcher;

public class CCSParser {

    private List<Process> processes;
    private boolean implicitNull = false;
    private int paren;
    public CCSParser(){
        this.paren = 0;
    }

    public CCSParser(boolean implicitNull){
        this.implicitNull = implicitNull;
    }


    public ProcessTemplate parseLine(String line){
        StringWalker walker = new StringWalker(line);
        ProcessTemplate template = new ProcessTemplate();
        while(walker.canWalk()){
            System.out.println(walker.readMemory());
            walker.walk();
            StringBuilder sb = new StringBuilder();
            for (CCSGrammar g : CCSGrammar.values()) {
                if (g.getClassObject() == null || g == CCSGrammar.OP_ACTIONPREFIX || g == CCSGrammar.LABEL || g == CCSGrammar.PROCESS)
                    continue;
                Matcher m = g.match(walker.readMemory());
                //System.out.println(String.format("Found match: [%s] %s", g.name(), m.group()));
                if (m.find()){
                    System.out.print("\nFound match!: " + m.group() + " INDEX: " + g.name());
                    if (g == CCSGrammar.ACTIONPREFIX_COMPLETE) {
                        System.out.println("Parsing into action prefix...");
                        template.add(ActionPrefixProcessFactory.parse(m.group()));
                    }else if (g == CCSGrammar.OP_CONCURRENT){
                        System.out.println("Parsing into concurrent...");
                        template.add(new ConcurrentProcess(null,null));
                    }else if (g == CCSGrammar.OP_SUMMATION){
                        System.out.println("Parsing into summation...");
                        template.add(new SummationProcess(null,null));
                    }/*else if (g == CCSGrammar.PROCESS){
                        *//**
                         * I was thinking about completely removing the distinction between processes and null
                         * processes, but am unsure if that's the right way to go. If it turns out there is
                         * no need for distinction, PROCESS can be changed to [A-Z0], and nothing else changes
                         *//*
                        template.add(ProcessFactory.determineRestriction(m.group()));
                    }else if (g == CCSGrammar.NULL_PROCESS){
                        template.add(new NullProcess());
                    }*/
                        //template.add();
                    else if (g == CCSGrammar.PROCESS || g == CCSGrammar.NULL_PROCESS){
                        System.out.println("Parsing into process...");
                        continue;
                        //Do nothing for processes for now. I could change by rearranging CCSGRAMMAR precedence, but this is easier
                        //and if I need to process them separately in the future this will be the way.
                    }else if (g == CCSGrammar.OPEN_PARENTHESIS){

                    }
                    walker.clearMemory();
                }
            }

        }
        return template;
    }

    private int checkEndParenthesis(String s){
        StringWalker w = new StringWalker(s);
        boolean open = false;
        while(w.canWalk()){
            w.walk();

            if (CCSGrammar.OPEN_PARENTHESIS.match(Character.toString(w.read())).find()){

            }
        }
        return -1;
    }



}
