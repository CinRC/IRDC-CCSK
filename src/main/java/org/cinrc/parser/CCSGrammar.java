package org.cinrc.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum CCSGrammar {
  COMPLEMENT_MARKER("'", false, -1),
  LABEL_IN("[a-z]", false, -1),
  LABEL_OUT(COMPLEMENT_MARKER.pString + LABEL_IN.pString, false, -1),
  LABEL_TAU("Tau\\{("+LABEL_IN.pString+")\\}", false, 2),

  LABEL_ANY(either(LABEL_IN, LABEL_OUT, LABEL_TAU), true, 1),
  DIGITS("\\d+", false, -1),

  OPEN_PAR("\\(", true, 0),
  CLOSE_PAR("\\)", true, 0),
  LABEL_SUFFIX("\\[k" + DIGITS.pString + "\\]", false, -1), //Must be found before labels
  LABEL_KEY(LABEL_ANY.pString+ LABEL_SUFFIX.pString, true, 3),
  OP_SEQ("\\.", true, 0),
  OP_PAR("\\|", true, 0),
  OP_SUM("\\+", true, 0),
  PROC_NUL("0", true, 0),
  PROC_NAM("[A-Z]", true, 0),
  RESTRICTION(String.format("\\\\\\{(%s+)(,\\s*%s+)*\\}", LABEL_IN.pString, LABEL_IN.pString), true, 2);


  public String pString;
  private final boolean canParse;

  private final int priority;

  CCSGrammar(String s, boolean canParse, int priority) {
    this.pString = s;
    this.canParse = canParse;
    this.priority = priority;
  }

  public boolean canBeParsed() {
    return this.canParse;
  }

  public int getPriority(){
    return priority;
  }

  public Matcher match(CharSequence c) {
    return Pattern.compile(pString).matcher(c);
  }


  static String either(CCSGrammar... grammars){
    String s = "(";
    String[] collect = new String[grammars.length];
    for (int i = 0; i < grammars.length; i++){
      collect[i] = "("+grammars[i].pString+")";
    }
    s += String.join("|", collect);
    s += ")";
    return s;
  }

  public static List<CCSGrammar> sort(){
    CCSGrammar[] grammars = CCSGrammar.values();
    Arrays.sort(grammars, Comparator.comparing(CCSGrammar::getPriority).reversed());
    List<CCSGrammar> l = new ArrayList<CCSGrammar>(Arrays.asList(grammars));
    l.removeIf(c -> !c.canParse);
    return l;
  }

}
