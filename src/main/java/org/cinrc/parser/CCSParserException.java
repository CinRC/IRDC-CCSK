package org.cinrc.parser;

public class CCSParserException extends RuntimeException {

  /**
   * Construct new empty parsing exception
   */
  public CCSParserException() {
    super();
  }

  public CCSParserException(String s) {
    super(s);
  }

  /**
   * Constructs an exception from a failed string match and pattern.
   *
   * @param s - String that failed to match
   * @param g - Grammar that attempted to match
   */
  public CCSParserException(String s, CCSGrammar g) {
    super(String.format("Could not match %s to %g", s, g));
  }


}
