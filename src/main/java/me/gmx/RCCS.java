package me.gmx;

import me.gmx.parser.CCSParser;
import me.gmx.process.ProcessTemplate;

import java.util.*;

public class RCCS {
    static Scanner scan;
    //Print debug info?
    public static final boolean DEBUG = true;
    public static final boolean UNIQUE_CHANNELS = false;
    //Should reversal keys be treated as actionable labels?
    public static final boolean KEYS_AS_LABELS = true;
    //Should labels be *visibly* different? (a0 a1 vs a0 a1)
    public static final boolean DIFFERENTIATE_LABELS = false;
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

    public enum RCCSFlag{
        DEBUG("Should program print debug info", "--debug",false),
        UNIQUE_CHANNELS("[broken] Should each channel's identity be dictated by it's unique ID",
                "--uC",false),
        KEYS_AS_LABELS("Should CCSK keys be treated as labels",
                "--kL", true),
        DIFFERENTIATE_LABELS("Should labels be given numerical identifiers to differentiate them",
                "--dL", false),
        KEY_MATCHING_MODE("[broken] Should a label's identity be determined by its unique ID?",
                "--kM", false), //0 = true 1 = false
        SUMMATION_STYLE_1("Alternative display mode for summation processes. Reversible summations are not annotated",
                "--sA", false),
        SUMMATION_STYLE_2("Alternative display mode for summation processes. Reversible summations are annotated",
                "--sB", true),
        SUMMATION_STYLE_3("Alternative display mode for summation processes. Reversible summations are hidden after execution",
                "--sC", false),
        IMPLICIT_NULL("Should labels without processes attached be implicitly implied to have a trailing null process",
                "--iN", true),
        DISPLAY_PARENTHESIS("Should complex processes display parenthesis to group its components",
                "--dP", true),
        DISPLAY_NULL("Should null processes be displayed?",
                "dN", false);


        private String description;

        RCCSFlag(String desc, String flag, boolean defState){
            description = desc;
        }

    }

}
