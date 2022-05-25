package me.gmx;

import me.gmx.parser.CCSParser;
import me.gmx.process.ProcessTemplate;
import me.gmx.process.nodes.LabelNode;
import me.gmx.process.nodes.LabelNodeFactory;

import java.util.*;

public class RCCS {
    static Scanner scan;
    public static final boolean DEBUG = true;
    public static void main(String[] args){
        String formula = "";
        Set<CCSFlag> flags = new HashSet<>();
        if (args.length == 0){
            log("Incorrect arguments! Please use the form `java -jar RCCS.jar <opt/flags> \"a.b|c.a\"");
            System.exit(1);
        }

        if (args.length > 1)
            for (CCSFlag flag : CCSFlag.values())
                for (int i = 0; i < args.length;i++)
                    if (flag.match(args[i]))
                        flags.add(flag);

        CCSParser c = new CCSParser();
        try {
            ProcessTemplate template = c.parseLine(args[flags.size()]);
            log(String.format("Formula before complex init and minimization: %s", template.prettyString()));
            log("\nMinimizing and initializing function...");
            template.initComplex();
            log(String.format("Formula after complex init and minimization: %s", template.prettyString()));
            if (CCSFlag.INTERACTIVE.check(flags))
                new CCSInteractionHandler(template,flags).startInteraction();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void log(String s){
        if (RCCS.DEBUG)
            System.out.println("[debug] " + s);
    }

    /**
     * Placeholder enum for flags passed in through CLI. Easier to look at this than
     * a switch statement on strings
     */
    enum CCSFlag{

        ENUMERATE("-e","Starts an enumerative session", false, "--enumerate"),
        INTERACTIVE("-i", "Creates an interactive session over the formula", true, "--interactive"),
        PRINT_LABELS("-p","Whether or not to print actionable labels at each stage", true, "--printlabels","--printlabel");

        private Collection<String> aliases;
        private String flag, desc;
        private boolean def;
        CCSFlag(String single, String desc, boolean def, String... multiple){
            flag = single;
            this.desc = desc;
            aliases = Set.of(multiple);
            this.def = def;
        }

        /**
         * Check if input matches any flag, and if so return the flag
         * @param s String to match
         * @return Any matching flag
         */
        static CCSFlag getFlag(String s){
            for (CCSFlag flag : values())
                if (flag.match(s))
                    return flag;
            return null;
        }

        boolean match(String s){
            return this.flag.equals(s) || this.aliases.contains(s);
        }

        /**
         * Checks and defaults
         * @param flags
         * @return
         */
        public boolean check(Collection<CCSFlag> flags){
            return flags.contains(this) ? true : this.def;
        }

        /**
         * Build a nicely formatted string to be printed during a help or usage message
         * @return
         */
        String buildHelperString(){
            //[single flag] <[default value]>, [longer flags,...] - [description]
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("%s <%s>",this.flag,String.valueOf(this.def)));
            sb.append(",");
            for (String s : this.aliases)
                sb.append(s+", ");
            sb.delete(sb.length()-2,sb.length());
            sb.append("- " + this.desc);
            return sb.toString();
        }

    }





}
