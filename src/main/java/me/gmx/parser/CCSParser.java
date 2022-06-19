package me.gmx.parser;

import me.gmx.RCCS;
import me.gmx.thread.ProcessTemplate;
import me.gmx.process.nodes.Label;
import me.gmx.process.nodes.LabelFactory;
import me.gmx.process.process.*;
import me.gmx.util.RCCSFlag;
import me.gmx.util.SetUtil;

import java.util.Collection;
import java.util.LinkedList;
import java.util.regex.Matcher;

public class CCSParser {

    public CCSParser(){
    }



    public static ProcessTemplate parseLine(String line){
        RCCS.log("Starting parsing of " + line);
        StringWalker walker = new StringWalker(line);
        walker.setIgnore(' ');

        ProcessTemplate template = new ProcessTemplate();
        int counter = 0;
        boolean inParenthesis = false;
        boolean inSetNotation = false;
        LinkedList<Label> prefixes = new LinkedList<>();
        LinkedList<Label> restrictions = new LinkedList<>();

        do{
            walker.walk();
            RCCS.log(String.format("Begin matching with memory %s, counter: %d, set: %b",walker.readMemory(), counter, inSetNotation));

            if (CCSGrammar.OPEN_PARENTHESIS.match(String.valueOf(walker.read())).find()) {
                counter++;
                //If it's the first parenthesis (opening) then delete first one
                inParenthesis = true;
            }

            if (inParenthesis) {
                if (CCSGrammar.CLOSE_PARENTHESIS.match(String.valueOf(walker.read())).find()) {
                    counter--;
                    if (counter == 0) {
                        template.add(parseLine(walker.readMemory()//sub to remove paren
                                .substring(1,walker.readMemory().length()-1)).export()); //while in parenthesis, everything gets auto-packed.
                        inParenthesis = false;
                        walker.clearMemory();
                    }
                }
            }
                if (!inParenthesis)
                for (CCSGrammar g : CCSGrammar.values()) {
                if (!g.canBeParsed())
                    continue;
                Matcher m = g.match(walker.readMemory());
                if (m.find()){
                    RCCS.log("Found match: " + m.group() + " Grammar: " + g.name());
                    //If
                    if (!RCCS.config.contains(RCCSFlag.IGNORE_UNRECOGNIZED))
                        if (!g.match(walker.readMemory()).matches()) {
                            String tempString = walker.readMemory().replace(m.group(),"");
                            System.out.println("Unrecognized character(s): " + tempString);
                            System.exit(1);
                        }

                    if (inSetNotation){
                        if (g == CCSGrammar.LABEL_COMBINED){
                            restrictions.add(LabelFactory.parseNode(m.group())); //Add restriction to list
                            if (walker.peek().equals(",")) //If the next symbol is a comma, just skip and forget about it
                                walker.walk(false);
                        }else if (g == CCSGrammar.CLOSE_RESTRICTION){
                            inSetNotation = false;
                            template.addRestrictionToLastProcess(restrictions);
                            restrictions.clear();
                        }
                        walker.clearMemory();
                        continue;
                    }
                    if (g == CCSGrammar.LABEL_COMBINED) { //a , 'b , c
                        RCCS.log("Adding prefix: " + m.group());
                        prefixes.add(LabelFactory.parseNode(m.group()));
                        if (!walker.canWalk()){
                            template.add(new ActionPrefixProcess(new NullProcess(), prefixes));
                            prefixes.clear();
                        }else if (walker.peek().equals(CCSGrammar.OP_SEQUENTIAL.toString())) { //If there is a . after label, then skip over it and continue.
                            walker.walk(false);
                        //If there is no ., then treat it as an implicit "0" process
                        }else {
                            if (!RCCS.config.contains(RCCSFlag.EXPLICIT_NULL)){
                                template.add(new ActionPrefixProcess(new NullProcess(), prefixes));
                                prefixes.clear();
                            }else
                                throw new CCSParserException("Could not find process for prefixes: " + SetUtil.csvSet(prefixes));
                        }
                    }else if (g == CCSGrammar.PROCESS){
                        if (prefixes.isEmpty())
                            template.add(new ProcessImpl(walker.readMemory()));
                        else {
                            template.add(new ActionPrefixProcess(new ProcessImpl(walker.readMemory()), prefixes));
                            prefixes.clear();
                        }
                    }else if (g == CCSGrammar.NULL_PROCESS){
                        if (prefixes.isEmpty())
                            template.add(new NullProcess());
                        else {
                            template.add(new ActionPrefixProcess(new NullProcess(), prefixes));
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
                    }else if (g == CCSGrammar.OPEN_RESTRICTION){ //\{a,b}
                        if (!inSetNotation)
                            inSetNotation = true;
                        else throw new CCSParserException("Unexpected token: " + walker.readMemory());
                    }


                    walker.clearMemory();
                }
            }

        }while(walker.canWalk());

        return template;
    }


    public Collection<Label> determineRestrictions(String s){
        return null;
    }

}
