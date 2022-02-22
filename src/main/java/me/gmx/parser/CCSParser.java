package me.gmx.parser;

import me.gmx.process.process.ProcessFormula;

public class CCSParser {

    public CCSParser(String text){
        for (String line : text.lines().toList())
            parseLine(line);

    }

    public ProcessFormula parseLine(String line){
        StringWalker walker = new StringWalker(line);
        while(walker.canWalk()){
            walker.walk();

        }
        //TODO
        return null;
    }



}
