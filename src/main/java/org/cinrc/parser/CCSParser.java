package org.cinrc.parser;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.regex.Matcher;
import org.cinrc.IRDC;
import org.cinrc.process.ProcessTemplate;
import org.cinrc.process.nodes.Label;
import org.cinrc.process.nodes.LabelFactory;
import org.cinrc.process.process.ConcurrentProcess;
import org.cinrc.process.process.NullProcess;
import org.cinrc.process.process.Process;
import org.cinrc.process.process.ProcessImpl;
import org.cinrc.process.process.SummationProcess;
import org.cinrc.util.RCCSFlag;
import org.cinrc.util.SetUtil;

public class CCSParser {

  public CCSParser() {
  }

  /**
   * Primary parsing function of this program. This method will parse
   * the given input string and return a ProcessTemplate representing that
   * string, if successful.
   *
   * @param line String representing process in proper format
   * @return ProcessTemplate representing that string
   */
  public static ProcessTemplate parseLine(String line) {
    IRDC.log("Starting parsing of " + line);
    StringWalker walker = new StringWalker(line);
    walker.setIgnore(' ');

    ProcessTemplate template = new ProcessTemplate();
    int counter = 0;
    boolean inParenthesis = false;
    boolean inSetNotation = false;
    LinkedList<Label> prefixes = new LinkedList<>();
    LinkedList<Label> restrictions = new LinkedList<>();

    do {
      walker.walk();
      IRDC.log(
          String.format("Begin matching with memory %s, counter: %d, set: %b", walker.readMemory(),
              counter, inSetNotation));

      //If parenthesis
      if (CCSGrammar.OPEN_PARENTHESIS.match(String.valueOf(walker.read())).find()) {
        counter++; //Increment counter to adjust parenthesis depth
        inParenthesis = true;
        continue;
      }

      if (inParenthesis) {
        if (CCSGrammar.CLOSE_PARENTHESIS.match(String.valueOf(walker.read())).find()) {
          counter--;
          if (counter == 0) {
            Process dp = CCSParser.parseLine(
                walker.readMemory().substring(1, walker.readMemory().length() - 1)).export();
            dp.addPrefixes(prefixes);
            template.add(dp);
            inParenthesis = false;
            walker.clearMemory();
          }
        }
      } else {
        for (CCSGrammar g : Arrays.stream(CCSGrammar.values()).filter(c -> c.canBeParsed())
            .toList()) {
          Matcher m = g.match(walker.readMemory());
          if (!m.find()) {
            continue;
          }
          //If grammar matches but does not match *everything* in memory
          if (!g.match(walker.readMemory()).matches()) {
            throw new CCSParserException(
                "Unrecognized character(s): " + walker.readMemory().replace(m.group(), ""));
          }
          IRDC.log("Found match: " + m.group() + " Grammar: " + g.name());
          if (inSetNotation) {
            if (g == CCSGrammar.LABEL_COMBINED) { //Is there a label here
              restrictions.add(LabelFactory.parseNode(m.group())); //Add restriction to list
              if (walker.peek()
                  .equals(",")) { //If the next symbol is a comma, just skip and forget about it
                walker.walk(false);
              }
            } else if (g == CCSGrammar.CLOSE_RESTRICTION) {
              inSetNotation = false;
              template.addRestrictionToLastProcess(restrictions);
              restrictions.clear();
            } else {
              throw new CCSParserException(
                  "Found unrecognized character(s) inside restriction: " + walker.readMemory());
            }
            walker.clearMemory();
            continue;
          }

          switch (g) {
            case LABEL_COMBINED:
              IRDC.log("Adding prefix: " + m.group());
              prefixes.add(LabelFactory.parseNode(m.group()));
              if (!walker.canWalk()) {
                template.add(new NullProcess(prefixes));
                prefixes.clear();
                //If there is a . after label, then skip over it and continue.
              } else if (walker.peek().equals(CCSGrammar.OP_SEQUENTIAL.toString())) {
                walker.walk(false);
                //If there is no ., then treat it as an implicit "0" process
              } else if (!IRDC.config.contains(RCCSFlag.REQUIRE_EXPLICIT_NULL)) {
                template.add(new NullProcess(prefixes));
                prefixes.clear();
              } else {
                throw new CCSParserException(
                    String.format("Could not find process for prefixes: %s",
                        SetUtil.csvSet(prefixes)));
              }
              break;

            case PROCESS:
              if (prefixes.isEmpty()) {
                template.add(new ProcessImpl(walker.readMemory()));
              } else {
                template.add(new ProcessImpl(walker.readMemory(), prefixes));
                prefixes.clear();
              }
              break;
            case NULL_PROCESS:
              if (prefixes.isEmpty()) {
                template.add(new NullProcess());
              } else {
                template.add(new NullProcess(prefixes));
                prefixes.clear();
              }
              break;
            case OP_CONCURRENT:
              template.add(new ConcurrentProcess(null, null));
              break;
            case OP_SUMMATION:
              template.add(new SummationProcess(null, null));
              break;
            case OPEN_RESTRICTION:
              if (!inSetNotation) {
                inSetNotation = true;
              } else {
                throw new CCSParserException(
                    String.format("Unexpected token: %s", walker.readMemory()));
              }
              break;
          }
          walker.clearMemory();
        }
      }

    } while (walker.canWalk());

    return template;
  }
}
