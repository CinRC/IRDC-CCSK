package org.cinrc.process.nodes;

import java.util.regex.Matcher;
import org.cinrc.IRDC;
import org.cinrc.parser.CCSParserException;
import org.cinrc.parser.CCSGrammar;

public class LabelFactory {

  /**
   * Simply matches the first node, ignores everything else
   *
   * @param s String to parse
   * @return Label parsed from the given strings
   */
  public static Label parseNode(String s) {
    IRDC.log("Beginning parse of node %s", s);
    Matcher m = CCSGrammar.LABEL_KEY.match(s);
    if (m.find()){ //Is this a key?
      IRDC.log("Detected key in %s", s);
      Label l = LabelFactory.parseNode(
          s.replaceAll(CCSGrammar.LABEL_SUFFIX.pString, "")); //Just label
      m = CCSGrammar.DIGITS.match(s);
      if (!m.find()){
        throw new CCSParserException("Could not find key dupe in key " + s);
      }
      IRDC.log("Generated new key from %s", s);
      return new LabelKey(l, Integer.parseInt(m.group()));
    }

    m = CCSGrammar.LABEL_TAU.match(s);
    if (m.find()){
      String c = stripTau(s);
      m = CCSGrammar.LABEL_IN.match(c);
      if (!m.find()){
        throw new CCSParserException("Cannot find label in tau " + s);
      }
      return new TauLabelNode(m.group());
    }

    m = CCSGrammar.LABEL_OUT.match(s);
    if (m.find()) {
      return new ComplementLabelNode(m.group().replace("'",""));
    }
    m = CCSGrammar.LABEL_IN.match(s);
    if (m.find()) {
      return new LabelNode(m.group());
    }


    throw new CCSParserException(String.format("Could not parse %s into labels", s));
  }

  /**
   * Creates a 'debug' label. Debug labels are created for testing. Their dupeId
   * is set to -1, which is not normally possible. All debug labels will evaluate to be
   * equal to another label if both labels are along the same channel, regardless of dupe
   *
   * @param channel
   * @return
   */
  public static Label createDebugLabel(String channel) {
    Label c = parseNode(channel);
    c.dupe = -1;
    c.isDebugLabel = true;
    return c;
  }


  public static LabelNode createLabelNode(String channel){
    return new LabelNode(channel);
  }

  public static LabelNode createDebugLabelNode(String channel){
    LabelNode n = new LabelNode(channel);
    n.dupe = -1;
    return n;
  }

  public static ComplementLabelNode createDebugComplementLabelNode(String channel){
    ComplementLabelNode n = new ComplementLabelNode(channel);
    n.dupe = -1;
    return n;
  }

  public static ComplementLabelNode createComplementLabelNode(String channel){
    return new ComplementLabelNode(channel);
  }

  public static LabelKey createDebugLabelKey(Label label) {
    LabelKey c = new LabelKey(label);
    c.dupe = -1;
    c.isDebugLabel = true;
    return c;
  }

  public static Label createLabel(String channel, int dupe) {
    Label c = parseNode(channel);
    c.dupe = dupe;
    return c;
  }


  public static String stripTau(String s){
    //Tau{a...}
    //Strips first 4 and last 1 characters
    Matcher m = CCSGrammar.LABEL_TAU.match(s);
    if (!m.find())
      throw new CCSParserException("Attempted to strip tau from " + s + " but found no tau.");
    return m.group(1);
  }
}
