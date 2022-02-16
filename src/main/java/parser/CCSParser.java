package parser;

import process.CCSGrammar;
import process.ProcessFormula;
import process.ProcessNode;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
