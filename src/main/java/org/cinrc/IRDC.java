package org.cinrc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import javafx.util.Pair;
import org.cinrc.UI.GUIThread;
import org.cinrc.UI.RCCS_FX;
import org.cinrc.parser.CCSParser;
import org.cinrc.parser.CCSParserException;
import org.cinrc.parser.LTTNode;
import org.cinrc.process.ProcessTemplate;
import org.cinrc.process.process.Process;
import org.cinrc.util.RCCSFlag;

public class IRDC {
  public static List<RCCSFlag> config = new ArrayList<>();
  public static List<String> proofNonSimulation = new ArrayList<>();

  public static void main(String[] args) {
    if (args.length == 0) {
      startGUI();
      System.exit(0);
    } else if (args.length == 1) {
      if (args[0].equals(RCCSFlag.HELP_MSG.getFlag())) {
        System.out.println(helpMessage());
        System.exit(0);
      }
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
      validate(args[args.length - 1]);
    } else if (config.contains(RCCSFlag.ENUMERATE)) {
      enumerate(args[args.length - 1]);
    } else if (config.contains(RCCSFlag.INTERACTIVE)) {
      interactive(args[args.length - 1]);
    } else if (config.contains(RCCSFlag.EQUIVALENCE)) {
      equivalence(args[args.length - 1]);
    }else if (config.contains(RCCSFlag.REGENERATE)) {
      regenerate(args[args.length - 1]);
    } else {
      enumerate(args[args.length - 1]);
    }
    System.exit(0);


  }

  private static void regenerate(String args) {
    org.cinrc.process.process.Process p =
        new CCSParser().parseLine(args);
    LTTNode node = new LTTNode(p);
    node.regenerate();
    try {
      node = node.getAncestor();
    }catch(Exception e){
      e.printStackTrace();
    }
    node.enumerate(true);
    System.out.println(node);
    System.exit(0);
  }

  private static void validate(String args) {
    Path path = null;
    try {
      path = Paths.get(args);
      List<String> allLines = Files.readAllLines(path);
      for (String line : allLines) {
        try {
          Process p = new CCSParser().parseLine(line);
          System.out.printf("%s is properly formatted. Parsed as %s\n", line,
              p.represent());
        } catch (Exception e) {
          System.out.printf("%s is improperly formatted\n", line);
          if (config.contains(RCCSFlag.DEBUG)) {
            e.printStackTrace();
          }
        }
      }
      System.exit(0);
    } catch (IOException e) {
      System.out.println("An error occurred while trying to read file: " + path);
    }
  }

  private static void enumerate(String args) {
    org.cinrc.process.process.Process p =
        new CCSParser().parseLine(args);
    LTTNode node = new LTTNode(p);
    node.enumerate(true);
    System.out.println(node);
    System.exit(0);
  }

  private static void interactive(String args) {
    try {
      Process p = new CCSParser().parseLine(args);
      new CCSInteractionHandler(p).startInteraction();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static void equivalence(String args) {
    //Default:
    ArrayList<Process> processes = new ArrayList<>();
    String[] formula = args.split(",");
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < formula.length; i++) {
      try {
        Process p = new CCSParser().parseLine(formula[i]);
        sb.append(String.format("[%d] %s => Parsed Successfully.\n", i, p.represent()));
        processes.add(p);
      } catch (CCSParserException e) {
        System.out.printf("%s is not properly formatted! Please check formatting guidelines.",
            formula[i]);
        System.exit(1);
      }
    }
    sb.append("\n\n");
    List<Pair<String, String>> simulates = new ArrayList<>();
    for (Process p : processes) {
      for (Process p2 : processes) {
        if (p == p2) {
          continue;
        }
        if (p2.simulates(p)) {
          simulates.add(new Pair(p2.represent(), p.represent()));
        }
      }
    }
    sb.append("Simulations and Bisimulations: \n ------------\n");
    for (Pair<String, String> e : simulates) { //Print simulations
      Pair<String, String> newPair = new Pair<String,String>(e.getValue(),e.getKey());
      if (simulates.contains(newPair)){
        sb.append(String.format("%s %s %s\n", e.getKey(), "≈", e.getValue()));
        simulates.remove(newPair);
      }else{
        sb.append(String.format("%s %s %s\n", e.getValue(), "≲", e.getKey()));
      }

    }
    System.out.println(sb);

    if (!proofNonSimulation.isEmpty()){
      System.out.println("Proof of non-simulation:");
      for (String s : proofNonSimulation){
        System.out.println(s);
      }
    }

  }

  private static void startGUI() {
    Application.launch(RCCS_FX.class);
  }

  public static void log(String s, Object... objects) {
    if (IRDC.config.contains(RCCSFlag.DEBUG)) {
      System.out.printf("[debug] " + s + "%n", objects);
    }
  }

  public static String helpMessage() {
    StringBuilder sb = new StringBuilder();
    for (RCCSFlag flag : RCCSFlag.values()) {
      sb.append(flag.getFlag()).append("  -  ").append(flag.getDescription()).append("\n");
    }

    sb.append("Enumerate Mode (default)\n---------------\n");
    sb.append("In enumeration mode, the given process will be enumerated to completion, " +
        "and the transition tree will be printed to stdout\n");
    sb.append("\nExamples:\n");
    sb.append("java -jar IRDC.jar --enumerate \"(a.b+'a.c)|(b.'a+'a.'b)\"\n");

    sb.append("Interactive Mode\n---------------\n");
    sb.append("In interactive mode, the given process will be displayed and the user will " +
        "be prompted to give a label or key to act on.\n");
    sb.append("\nExamples:\n");
    sb.append("java -jar IRDC.jar --interactive \"a.b + 'a.c\"\n");
    sb.append("java -jar IRDC.jar --interactive \"(a.c|'a.c\\{a}) + 'a|c.b\"\n");

    sb.append("Equivalence Mode\n---------------\n");
    sb.append("In equivalence mode, your input will be in the form of a *list* of processes, " +
        "separated by commas (,). All equivalence relationships between the given processes " +
        "will be printed.\n");
    sb.append("\nExamples:\n");
    sb.append("java -jar IRDC.jar --equivalence \"a.b.c + a.b.d,a.(b.c+b.d)\"\n");
    sb.append("java -jar IRDC.jar --equivalence \"a|b,a.b\"\n");


    sb.append("Validation Mode\n---------------\n");
    sb.append("In validation mode, a user inputted file will be scanned for processes to parse. " +
        "Processes in the file must be separated by newlines, with one process per line. " +
        "Each process will be validated for syntax and formatting.\n");
    sb.append("\nExamples:\n");
    sb.append("java -jar IRDC.jar --validate processes.txt");

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
