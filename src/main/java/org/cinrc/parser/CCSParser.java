package org.cinrc.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.regex.Matcher;
import org.cinrc.IRDC;
import org.cinrc.process.ProcessTemplate;
import org.cinrc.process.nodes.Label;
import org.cinrc.process.nodes.LabelFactory;
import org.cinrc.process.nodes.LabelKey;
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
    boolean inKeyNotation = false;
    boolean inSetNotation = false;
    LinkedList<Label> prefixes = new LinkedList<>();
    LinkedList<Label> restrictions = new LinkedList<>();
    LinkedList<LabelKey> keys = new LinkedList<>();

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
          /*//If grammar matches but does not match *everything* in memory
          if (!g.match(walker.readMemory()).matches()) {
            throw new CCSParserException(
                "Unrecognized character(s): " + walker.readMemory().replace(m.group(), ""));
          }*/
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
            case LABEL_KEY_COMBINED:
              inKeyNotation = false;
              Matcher c = CCSGrammar.LABEL_COMBINED.match(walker.readMemory());
              if (!c.find())
                throw new CCSParserException("Could not find label in key");
              Label l = LabelFactory.parseNode(c.group(0));
              keys.add(new LabelKey(l));
              break;
            case LABEL_COMBINED:
              if (walker.peek().equals(CCSGrammar.OPEN_KEY_NOTATION.toString())){
                inKeyNotation = true;
                break;
              }else if (inKeyNotation){
                break;
              }
              IRDC.log("Adding prefix: " + m.group());
              prefixes.add(LabelFactory.parseNode(m.group()));
              if (!walker.canWalk()) {
                template.add(generateProcess(CCSGrammar.NULL_PROCESS.toString(), prefixes,keys));
                //If there is a . after label, then skip over it and continue.
              } else if (walker.peek().equals(CCSGrammar.OP_SEQUENTIAL.toString())) {
                walker.walk(false);
                //If there is no ., then treat it as an implicit "0" process
              } else if (!IRDC.config.contains(RCCSFlag.REQUIRE_EXPLICIT_NULL)) {
                template.add(generateProcess(CCSGrammar.NULL_PROCESS.toString(),prefixes,keys));
              } else {
                throw new CCSParserException(
                    String.format("Could not find process for prefixes: %s",
                        SetUtil.csvSet(prefixes)));
              }
              break;
            case PROCESS:
              template.add(generateProcess(walker.readMemory(), prefixes, keys));
              break;
            case NULL_PROCESS:
              if (inKeyNotation) //Keys can be k0, which matches to null process
                break;
                template.add(generateProcess(CCSGrammar.NULL_PROCESS.toString(), prefixes, keys));

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
          if (!inKeyNotation) {
            walker.clearMemory();
          }
        }
      }

    } while (walker.canWalk());

    return template;
  }

  public static LinkedList<Label> parseLabelsFromKeySet(LinkedList<LabelKey> keys){
    LinkedList<Label> l = new LinkedList<>();
    for (LabelKey k : keys){
      l.add(k.from);
    }
    return l;
  }

  public static Process generateProcess(String s, LinkedList<Label> prefixes, LinkedList<LabelKey> keys){
    Process p = null;
    if (CCSGrammar.NULL_PROCESS.match(s).find()){
      p = new NullProcess();
    }else if (CCSGrammar.PROCESS.match(s).find()){
      p = new ProcessImpl(s);
    }
    LinkedList<Label> parsedPrefixes = CCSParser.parseLabelsFromKeySet(keys);
    parsedPrefixes.addAll(prefixes);
    p.addPrefixes(parsedPrefixes);

    for (LabelKey key : keys){
      p.act(key.from);
    }

    prefixes.clear();
    keys.clear();
    return p;
  }

}
