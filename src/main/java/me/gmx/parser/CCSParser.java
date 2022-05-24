package me.gmx.parser;

import me.gmx.RCCS;
import me.gmx.process.ProcessTemplate;
import me.gmx.process.nodes.ActionPrefixProcessFactory;
import me.gmx.process.process.*;

import java.lang.Process;
import java.util.List;
import java.util.regex.Matcher;

public class CCSParser {

    private List<Process> processes;
    public CCSParser(){
    }



    public ProcessTemplate parseLine(String line){
        StringWalker walker = new StringWalker(line);
        walker.setIgnore(' ');

        ProcessTemplate template = new ProcessTemplate();
        int counter = 0;
        boolean inParenthesis = false;


        while(walker.canWalk()){
            walker.walk();
            RCCS.log(String.format("Begin matching with memory %s",walker.readMemory()));
            for (CCSGrammar g : CCSGrammar.values()) {
                /**"Skip if:
                 * 1. The grammar does not have an initializable class AND the grammar is not a parenthesis
                 * OR
                 * 2. The grammar is a LABEL or PROCESS
                 *
                 * It will skip labels and processes because I am CCSGrammar.ACTION
                 *
                 **/
                if ((g.getClassObject() == null && g != CCSGrammar.OPEN_PARENTHESIS && g != CCSGrammar.CLOSE_PARENTHESIS) || g == CCSGrammar.LABEL || g == CCSGrammar.PROCESS)
                    continue;

                //Start matching memory with grammar
                Matcher m = g.match(walker.readMemory());
                if (m.find()){
                    //While in parenthesis, do not match anything. Simply move to the closing parenthesis and send memory to this function for reparsing
                    if (inParenthesis) {
                        if (g == CCSGrammar.CLOSE_PARENTHESIS) {
                            counter--;
                            if (counter == 0) {
                                RCCS.log("Parenthesis close: " + walker.readMemory());
                                template.add(parseLine(walker.readMemory()//sub to remove paren
                                        .substring(1,walker.readMemory().length()-1)).export());
                                inParenthesis = false;
                            }
                        }else {
                            RCCS.log("Started, so no matching");
                            continue;
                        }
                    }

                    RCCS.log("\nFound match!: " + m.group() + " INDEX: " + g.name());

                    if (g == CCSGrammar.OPEN_PARENTHESIS){
                        counter++;
                        inParenthesis = true;
                    }else if (g == CCSGrammar.ACTIONPREFIX_COMPLETE) {
                        RCCS.log("Parsing into action prefix...");
                        template.add(ActionPrefixProcessFactory.parse(m.group()));
                    }else if (g == CCSGrammar.OP_CONCURRENT){
                        RCCS.log("Parsing into concurrent...");
                        template.add(new ConcurrentProcess(null,null));
                    }else if (g == CCSGrammar.OP_SUMMATION){
                        RCCS.log("Parsing into summation...");
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
                    if (!inParenthesis)
                    walker.clearMemory();
                }
            }

        }
        return template;
    }



}
