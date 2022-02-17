package parser;

import process.ProcessFormula;

import java.util.regex.Matcher;

public class CCSParser {

    public CCSParser(String text){
        for (String line : text.lines().toList())
            parseLine(line);

    }

    public ProcessFormula parseLine(String line){
        for (CCSGrammar grammar : CCSGrammar.values()){
            Matcher m = grammar.match(line);
            if (m.find())
                //TODO
                break;
        }
        //TODO
        return null;
    }



}
