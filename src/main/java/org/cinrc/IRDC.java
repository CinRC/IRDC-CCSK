package org.cinrc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import org.cinrc.UI.GUIThread;
import org.cinrc.UI.RCCS_FX;
import org.cinrc.parser.CCSParser;
import org.cinrc.parser.LTTNode;
import org.cinrc.process.ProcessContainer;
import org.cinrc.process.ProcessTemplate;
import org.cinrc.util.RCCSFlag;

public class IRDC {
    public static List<RCCSFlag> config = new ArrayList<>();

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println(helpMessage());
            System.exit(1);
        } else if (args.length > 1) {
            for (int i = 0; i < args.length - 1; i++) {
                RCCSFlag f = getFlagMatchOrNull(args[i]);
                if (f == null) {
                    System.out.println("Unrecognized config flag: " + args[i]);
                    System.exit(0);
                } else {
                    config.add(f);
                }
            }
        }
        if (config.contains(RCCSFlag.HELP_MSG)) {
            System.out.println(helpMessage());
            System.exit(0);
        } else if (config.contains(RCCSFlag.GUI)) {
            GUIThread t = new GUIThread("GUI");
            t.start();
        }
        if (config.contains(RCCSFlag.VALIDATE)) {
            Path path = null;
            try {
                path = Paths.get(args[config.indexOf(RCCSFlag.VALIDATE) + 1]);
                List<String> allLines = Files.readAllLines(path);
                for (String line : allLines) {
                    try {
                        ProcessTemplate pt = CCSParser.parseLine(line);
                        System.out.printf("%s is properly formatted. Parsed as %s\n", line,
                                pt.prettyString());
                    } catch (Exception e) {
                        System.out.printf("%s is improperly formatted\n", line);
                    }
                }
                System.exit(0);
            } catch (IOException e) {
                System.out.println("An error occurred while trying to read file: " + path);
            }

        } else if (config.contains(RCCSFlag.ENUMERATE)) {
            org.cinrc.process.process.Process p =
                    CCSParser.parseLine(args[config.indexOf(RCCSFlag.ENUMERATE) + 1]).export();
            LTTNode node = new LTTNode(p);
            node.enumerate(true);
            System.out.println(node);
            System.exit(0);
        }

        String formula = args[args.length - 1];
        CCSParser c = new CCSParser();

        try {
            ProcessTemplate template = CCSParser.parseLine(formula);
            log(String.format("Formula before complex init and minimization: %s",
                    template.prettyString()));
            log("\nMinimizing and initializing function...");
            template.initComplex();
            log(String.format("Formula after complex init and minimization: %s",
                    template.prettyString()));
            new CCSInteractionHandler(new ProcessContainer(template.export())).startInteraction();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static void startGUI() {
        Application.launch(RCCS_FX.class);
    }

    public static void log(String s) {
        if (org.cinrc.IRDC.config.contains(RCCSFlag.DEBUG)) {
            System.out.println("[debug] " + s);
        }
    }

    public static String helpMessage() {
        StringBuilder sb = new StringBuilder();
        sb.append(
                "Incorrect arguments! Please use the form `java -jar IRDC.jar <flags> \"a.b|c.a\"");
        sb.append("\n");
        for (RCCSFlag flag : RCCSFlag.values()) {
            sb.append(flag.getFlag()).append("  -  ").append(flag.getDescription()).append("\n");
        }
        sb.append("\n--- Examples ---");
        sb.append("java -jar IRDC.jar \"a.b + 'a.c\"");
        sb.append("java -jar IRDC.jar \"(a.c|'a.c\\{a}) + 'a|c.b\"");
        sb.append("java -jar IRDC.jar --enumerate \"(a.b+'a.c)|(b.'a+'a.'b)\"");
        return sb.toString();
    }

    public static RCCSFlag getFlagMatchOrNull(String s) {
        for (RCCSFlag flag : RCCSFlag.values()) {
            if (flag.doesMatch(s)) {
                return flag;
            }
        }

        return null;
    }

}