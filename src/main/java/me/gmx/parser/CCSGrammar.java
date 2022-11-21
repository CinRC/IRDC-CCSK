package me.gmx.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import me.gmx.process.nodes.ComplementLabelNode;
import me.gmx.process.nodes.Label;
import me.gmx.process.nodes.LabelNode;
import me.gmx.process.nodes.ProgramNode;
import me.gmx.process.process.ActionPrefixProcess;
import me.gmx.process.process.ConcurrentProcess;
import me.gmx.process.process.NullProcess;
import me.gmx.process.process.ProcessImpl;
import me.gmx.process.process.SummationProcess;

public enum CCSGrammar {

  LABEL("[a-z]", LabelNode.class, null, false),
  WHITESPACE(" ", null, " ", false),
  OPEN_PARENTHESIS("\\(", null, "(", true),
  CLOSE_PARENTHESIS("\\)", null, ")", true),
  PROCESS("[A-Z]", ProcessImpl.class, null, true),
  NULL_PROCESS("[0]", NullProcess.class, "0", true),
  OP_SEQUENTIAL("\\.", null, ".", true),
  COMPLEMENT_SIG("'", null, "'", false),
  OUT_LABEL(String.format("'%s", LABEL.pString), ComplementLabelNode.class, null, false),
  OP_ACTIONPREFIX(String.format("(%s)|(%s)%s",
      LABEL.pString, OUT_LABEL.pString, OP_SEQUENTIAL.pString), null, null, false),
  LABEL_COMBINED(String.format("(%s)|(%s)",
      LABEL.pString, OUT_LABEL.pString), Label.class, null, true),
  OP_ACTIONPREFIX_REVERSE(
      String.format("%s(%s|%s)", OP_SEQUENTIAL.pString, LABEL.pString, OUT_LABEL.pString), null,
      null, false),
  ACTIONPREFIX_COMPLETE(String.format("(%s)*%s", OP_ACTIONPREFIX.pString, PROCESS.pString),
      ActionPrefixProcess.class, null, false),
  OP_CONCURRENT("\\|", ConcurrentProcess.class, "|", true),
  OP_SUMMATION("\\+", SummationProcess.class, "+", true),
  OPEN_RESTRICTION("\\\\\\{", null, null, true), //6 backslashes, LOL. \\{
  CLOSE_RESTRICTION("\\}", null, null, true);


  public static final Pattern parenthesisRegex;

  static {
    parenthesisRegex =
        Pattern.compile(String.format("([\\%s\\%s])", OPEN_PARENTHESIS, CLOSE_PARENTHESIS));
  }

  public final String pString, rep;
  private final Class<? extends ProgramNode> classObject;
  private final boolean canParse;

  /**
   * @param s        Regex to match against
   * @param c        Instantiatable class representation
   * @param rep      Human readable constant
   * @param canParse Should this be parseable
   */
  CCSGrammar(String s, Class<? extends ProgramNode> c, String rep, boolean canParse) {
    this.pString = s;
    this.classObject = c;
    this.rep = rep;
    this.canParse = canParse;
  }

  public Class<? extends ProgramNode> getClassObject() {
    return classObject;
  }

  public boolean canBeParsed() {
    return this.canParse;
  }

  /***
   * Returns pattern object from stored string, caching it if first access.
   * @return Pattern object
   */
  Pattern getPattern() {
    return Pattern.compile(this.pString);
  }

  public Matcher match(CharSequence c) {
    return getPattern().matcher(c);
  }

  public String toString() {
    return rep == null ? "" : rep;
  }


}
