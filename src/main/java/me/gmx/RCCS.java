package me.gmx;

import me.gmx.parser.CCSParser;
import me.gmx.process.ProcessTemplate;

import java.util.*;

public class RCCS {
    static Scanner scan;
    //Print debug info?
    public static final boolean DEBUG = true;
    //Should every channel parsed be given a unique ID? (This breaks things right now. I only leave it in for future use.
    public static final boolean UNIQUE_CHANNELS = false;
    //Should reversal keys be treated as actionable labels?
    public static final boolean KEYS_AS_LABELS = true;
    //0, 1 -> id, origin
    public static final int KEY_MATCHING_MODE = 0;
    //0 = keep summation, 1 = annotate summation, 2 = don't show summation
    public static final int SUMMATION_STYLE = 1;
    //Should null processes be implicit or explicit
    public static final boolean IMPLICIT_NULL_PROCESSES = true;
    //Should complex processes be displayed with parenthesis
    public static final boolean DISPLAY_PARENTHESIS = true;
    //Should null processes be displayed (a.0) or hidden (a)
    public static final boolean DISPLAY_NULL_PROCESSES = false;
    public static void main(String[] args){
        if (args.length == 0){
            log("Incorrect arguments! Please use the form `java -jar RCCS.jar \"a.b|c.a\"");
            System.exit(1);
        }
        CCSParser c = new CCSParser();
        try {
            ProcessTemplate template = c.parseLine(args[0]);
            log(String.format("Formula before complex init and minimization: %s", template.prettyString()));
            log("\nMinimizing and initializing function...");
            template.initComplex();
            log(String.format("Formula after complex init and minimization: %s", template.prettyString()));
            new CCSInteractionHandler(template).startInteraction();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void log(String s){
        if (RCCS.DEBUG)
            System.out.println("[debug] " + s);
    }

}
