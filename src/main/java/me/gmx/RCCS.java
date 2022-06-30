package me.gmx;

import me.gmx.parser.CCSParser;
import me.gmx.thread.ProcessContainer;
import me.gmx.thread.ProcessTemplate;
import me.gmx.util.RCCSFlag;

import java.util.*;

public class RCCS {
    static Scanner scan;

    public static List<RCCSFlag> config = new ArrayList<>();
    public static void main(String[] args){
        if (args.length == 0){
            System.out.println(helpMessage());
            System.exit(1);
        }else if (args.length == 1) {
            if (args[0].equals(RCCSFlag.HELP_MSG.getFlag())){
                System.out.println(helpMessage());
                System.exit(0);
            }
        }else{
            for (int i = 0; i < args.length-1; i++){
                RCCSFlag f = getFlagMatchOrNull(args[i]);
                if (f == null){
                    System.out.println("Unrecognized config flag: " + args[i]);
                    System.exit(0);
                }else
                    config.add(f);
            }
        }
        String formula = args[args.length-1];
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

    public static String helpMessage(){
        StringBuilder sb = new StringBuilder();
        sb.append("Incorrect arguments! Please use the form `java -jar RCCS.jar <flags> \"a.b|c.a\"");
        sb.append("\n");
        for (RCCSFlag flag : RCCSFlag.values()){
            sb.append(flag.getFlag() + "  -  " + flag.getDescription() + "\n");
        }
        return sb.toString();
    }

    public static RCCSFlag getFlagMatchOrNull(String s){
        for (RCCSFlag flag : RCCSFlag.values())
            if (flag.doesMatch(s))
                return flag;

        return null;
    }
    
}
