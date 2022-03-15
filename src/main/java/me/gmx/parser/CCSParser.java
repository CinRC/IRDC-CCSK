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
        walker.setIgnore(' ');
        ProcessTemplate template = new ProcessTemplate();
        int counter = 0;
        boolean started = false;
        while(walker.canWalk()){
            System.out.println(walker.readMemory());
            walker.walk();
            System.out.println(String.format("Begin matching with memory %s",walker.readMemory()));
            for (CCSGrammar g : CCSGrammar.values()) {
                if ((g.getClassObject() == null && g != CCSGrammar.OPEN_PARENTHESIS && g != CCSGrammar.CLOSE_PARENTHESIS) || g == CCSGrammar.OP_ACTIONPREFIX || g == CCSGrammar.LABEL || g == CCSGrammar.PROCESS)
                    continue;
                Matcher m = g.match(walker.readMemory());
                if (m.find()){

                    if (started) {
                        if (g == CCSGrammar.CLOSE_PARENTHESIS) {
                            counter--;
                            if (counter == 0) {
                                System.out.println("Parenthesis close: " + walker.readMemory());
                                template.add(parseLine(walker.readMemory()//sub to remove paren
                                        .substring(1,walker.readMemory().length()-1)).export());
                                started = false;
                            }
                        }else {
                            System.out.println("Started, so no matching");
                            continue;
                        }
                    }
                    System.out.println("\nFound match!: " + m.group() + " INDEX: " + g.name());

                    if (g == CCSGrammar.OPEN_PARENTHESIS){
                        counter++;
                        started = true;
                    }else if (g == CCSGrammar.ACTIONPREFIX_COMPLETE) {
                        System.out.println("Parsing into action prefix...");
                        template.add(ActionPrefixProcessFactory.parse(m.group()));
                    }else if (g == CCSGrammar.OP_CONCURRENT){
                        System.out.println("Parsing into concurrent...");
                        template.add(new ConcurrentProcess(null,null));
                    }else if (g == CCSGrammar.OP_SUMMATION){
                        System.out.println("Parsing into summation...");
                        template.add(new SummationProcess(null,null));
                    }/**
                         * I was thinking about completely removing the distinction between processes and null
                         * processes, but am unsure if that's the right way to go. If it turns out there is
                         * no need for distinction, PROCESS can be changed to [A-Z0], and nothing else changes
                         */
                    else if (g == CCSGrammar.PROCESS || g == CCSGrammar.NULL_PROCESS){
                        continue;
                        //Do nothing for processes for now. I could change by rearranging CCSGRAMMAR precedence, but this is easier
                        //and if I need to process them separately in the future this will be the way.
                    }
                    if (!started)
                    walker.clearMemory();
                }
            }

        }
        return template;
    }

    private ProcessTemplate handleParenthesis(String s){
        System.out.println(s);
        return parseLine(s);
    }

/*    private int checkEndParenthesis(String s){
        StringWalker w = new StringWalker(s);
        int open = 0;
        boolean start = false;
        while(w.canWalk()){
            w.walk();

            if (CCSGrammar.OPEN_PARENTHESIS.match(Character.toString(w.read())).find()){
                open++;
                start = true;
            }else if (CCSGrammar.CLOSE_PARENTHESIS.match(Character.toString(w.read())).find()){
                open--;
            }
            if (open == 0 && start){
                return w.readMemory();
            }
        }
        return "";
    }*/



}
