package org.cinrc.process.nodes;

import java.util.Collections;
import java.util.LinkedList;
import java.util.regex.Matcher;
import org.cinrc.parser.CCSGrammar;
import org.cinrc.parser.CCSParser;
import org.cinrc.parser.StringWalker;
import org.cinrc.process.process.ActionPrefixProcess;
import org.cinrc.process.process.Process;
import org.cinrc.process.process.ProcessImpl;

@Deprecated
public class ActionPrefixProcessFactory {


  /**
   * Creates new action prefix process from complete string
   *
   * @param s
   * @return
   */
  public static ActionPrefixProcess parseString(String s) {
    ActionPrefixProcess F_c = null;
    //Separate process from labels, assure it is last
    //IRDC.log("Creating prefix " + s);
    StringWalker w = new StringWalker(s);
    LinkedList<Label> prefixes = new LinkedList<>();
    Process process;
    do {
      w.walk();
      Matcher m = CCSGrammar.OP_ACTIONPREFIX.match(w.readMemory());
      //Forward through the labels until no more...
      if (m.find()) {
        prefixes.add(LabelFactory.parseNode(w.readMemory()));
        w.clearMemory();
      }
      //No more labels, must be a process now or breaks syntax
      //a.b.P
      //a.b.(c+z)
      //If acting as a sequential process, it MUST use parenthesis or it will fail
      if (w.look().startsWith("(")) {
        process = CCSParser.parseLine(w.look()).export();
      } else {
        process = new ProcessImpl(w.peek());
      }
    } while (w.canWalk());

    Collections.reverse(prefixes);
    F_c = new ActionPrefixProcess(process, prefixes);

    return F_c;
  }

}
