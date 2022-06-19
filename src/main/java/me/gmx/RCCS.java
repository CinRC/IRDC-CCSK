package me.gmx;

import me.gmx.parser.CCSParser;
import me.gmx.thread.ProcessContainer;
import me.gmx.thread.ProcessTemplate;
import me.gmx.util.RCCSFlag;

import java.util.*;

public class RCCS {
    static Scanner scan;
    //Print debug info?
/*    public static final boolean DEBUG = true;
    public static final boolean UNIQUE_CHANNELS = false;
    //Should reversal keys be treated as actionable labels?
    public static final boolean KEYS_AS_LABELS = true;
    //Should labels be *visibly* different? (a0 a1 vs a0 a1)
    public static final boolean DIFFERENTIATE_LABELS = true;
    //0, 1 -> id, origin
    public static final int KEY_MATCHING_MODE = 0;
    //0 = keep summation, 1 = annotate summation, 2 = don't show summation
    public static final int SUMMATION_STYLE = 1;
    //Should null processes be implicit or explicit
    public static final boolean IMPLICIT_NULL_PROCESSES = true;
    //Should complex processes be displayed with parenthesis
    public static final boolean DISPLAY_PARENTHESIS = true;
    //Should null processes be displayed (a.0) or hidden (a)
    public static final boolean DISPLAY_NULL_PROCESSES = false;*/

    public static List<RCCSFlag> config = new ArrayList<>();
    public static void main(String[] args){
        if (args.length == 0){
            System.out.println("Incorrect arguments! Please use the form `java -jar RCCS.jar \"a.b|c.a\"");
            System.exit(1);
        }
        int tempC = args.length;
        for (int i = 0; i < (args.length-1); i++) {
            for (RCCSFlag flag : RCCSFlag.values()) {
                if (flag.doesMatch(args[i])) {
                    config.add(flag);
                    tempC--;
                }
            }
        }

        if (tempC != 1){
            System.out.println("Unrecognized config!");
            System.exit(1);
        }
        String formula = args[args.length-1];

        if (RCCS.config.contains(RCCSFlag.HELP_MSG)){
            System.out.println("Help message:");
            System.exit(0);
        }


        CCSParser c = new CCSParser();
        try {
            ProcessTemplate template = c.parseLine(formula);
            log(String.format("Formula before complex init and minimization: %s", template.prettyString()));
            log("\nMinimizing and initializing function...");
            template.initComplex();
            log(String.format("Formula after complex init and minimization: %s", template.prettyString()));
            new CCSInteractionHandler(new ProcessContainer(template.export())).startInteraction();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void log(String s){
        if (RCCS.config.contains(RCCSFlag.DEBUG))
            System.out.println("[debug] " + s);
    }



}
