package me.gmx.util;

public enum RCCSFlag {
  DEBUG("Prints debug info",
      "--debug"),
  ENUMERATE("Prints enumeration tree of given process",
      "--enumerate"),
  HELP_MSG("Print this help message",
      "--help"),
  VALIDATE("Validate all given processes inside the file inputted after this flag", "--validate"),
  UNIQUE_CHANNELS("[broken] Should each channel's identity be dictated by it's unique ID",
      "--uC"),
  DIFFERENTIATE_LABELS("Labels are visibly differentiated by integers",
      "--dL"),
  HIDE_KEYS("CCSK keys are hidden",
      "--hide-keys"),
  KEY_MATCHING_MODE("[broken] Should a label's identity be determined by its unique ID?",
      "--kM"), //0 = true 1 = false
  SUMMATION_STYLE_1(
      "Alternative display mode for summation processes. Reversible summations are not annotated",
      "--sA"),
  SUMMATION_STYLE_3(
      "Alternative display mode for summation processes. Reversible summations are hidden after execution",
      "--sC"),
  EXPLICIT_NULL(
      "Labels explicitly require a trailing process. Labels will no longer have an implicit null process attached.",
      "--eN"),
  HIDE_PARENTHESIS("Parenthesis surrounding complex processes will be omitted",
      "--hP"),
  DISPLAY_NULL("Null processes will be displayed explicitly",
      "--dN"),
  IGNORE_UNRECOGNIZED("Unrecognized characters in process are ignored",
      "--iU"),
  KEYS_MATCH_LABELS("Keys will be visibly similar to the label they represent",
      "--kL"),
  GUI("Start program with GUI",
      "--gui");


  private final String description;
  private final String flagFlag;

  RCCSFlag(String desc, String flag) {
    description = desc;
    flagFlag = flag;
  }

  public boolean doesMatch(String s) {
    return s.equals(flagFlag);
  }

  public String getFlag() {
    return flagFlag;
  }

  public String getDescription() {
    return description;
  }


}