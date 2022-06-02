package me.gmx.parser;

import me.gmx.RCCS;
import me.gmx.process.ProcessTemplate;
import me.gmx.process.nodes.ActionPrefixProcessFactory;
import me.gmx.process.nodes.Label;
import me.gmx.process.nodes.LabelFactory;
import me.gmx.process.process.*;

import java.lang.Process;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;

public class CCSParser {

    public CCSParser(){
    }



    public static ProcessTemplate parseLine(String line){
        StringWalker walker = new StringWalker(line);
        walker.setIgnore(' ');

        ProcessTemplate template = new ProcessTemplate();
        int counter = 0;
        boolean inParenthesis = false;
        LinkedList<Label> prefixes = new LinkedList<>();

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
                if (
                        (g.getClassObject() == null
                        && g != CCSGrammar.OPEN_PARENTHESIS
                        && g != CCSGrammar.CLOSE_PARENTHESIS)

                        || g == CCSGrammar.LABEL
                        //|| g == CCSGrammar.PROCESS
                        || g == CCSGrammar.OUT_LABEL
                        || g == CCSGrammar.ACTIONPREFIX_COMPLETE

                )
                    continue;

                //Start matching memory with grammar
                Matcher m = g.match(walker.readMemory());
                if (m.find()){
                    RCCS.log("Found match: " + m.group() + " Grammar: " + g.name());
                    //While in parenthesis, do not match anything. Simply move to the closing parenthesis and send memory to this function for reparsing
                    if (inParenthesis) {
                        if (g == CCSGrammar.CLOSE_PARENTHESIS) {
                            counter--;
                            if (counter == 0) {
                                //RCCS.log("Parenthesis close: " + walker.readMemory());
                                template.add(parseLine(walker.readMemory()//sub to remove paren
                                        .substring(1,walker.readMemory().length()-1)).export());
                                inParenthesis = false;
                            }
                        }else {
                            //RCCS.log("Started, so no matching");
                            continue;
                        }

                    }
                    //RCCS.log("\nFound match!: " + m.group() + " INDEX: " + g.name());

                    if (g == CCSGrammar.OPEN_PARENTHESIS){
                        counter++;
                        inParenthesis = true;
                    }else if (g == CCSGrammar.LABEL_COMBINED) { //a , 'b , c
                        RCCS.log("Adding prefix: " + m.group());
                        prefixes.add(LabelFactory.parseNode(m.group()));
                        //If there is a . after label, then skip over it and continue.
                        if (walker.peek().equals(CCSGrammar.OP_SEQUENTIAL.toString())) {
                            walker.walk(false);
                        //If there is no ., then treat it as an implicit "Z" process
                        }else {
                            template.add(new ActionPrefixProcess(new ProcessImpl("Z"), prefixes));
                            prefixes.clear();
                        }
                    }else if (g == CCSGrammar.PROCESS){
                        if (prefixes.isEmpty())
                            template.add(new ProcessImpl(walker.readMemory()));
                        else {
                            template.add(new ActionPrefixProcess(new ProcessImpl(walker.readMemory()), prefixes));
                            prefixes.clear();
                        }
                    }else if (g == CCSGrammar.OP_CONCURRENT){
                        //RCCS.log("Parsing into concurrent...");
                        template.add(new ConcurrentProcess(null,null));
                    }else if (g == CCSGrammar.OP_SUMMATION){
                        //RCCS.log("Parsing into summation...");
                        template.add(new SummationProcess(null,null));
                    }else if (g == CCSGrammar.OP_SEQUENTIAL){
                        RCCS.log("Found sequential match: " + m.group());
                    }
                    if (!inParenthesis)
                        walker.clearMemory();
                }
            }

        }
        return template;
    }



}
