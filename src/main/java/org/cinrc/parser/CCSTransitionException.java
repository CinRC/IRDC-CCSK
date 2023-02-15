package org.cinrc.parser;

import org.cinrc.process.nodes.Label;
import org.cinrc.process.process.Process;

public class CCSTransitionException extends RuntimeException {

  public CCSTransitionException(Process p, Label l) {
    super(String.format("Could act %s on %s", p.represent(), l.toString()));
  }

  public CCSTransitionException(Label l) {
    super(String.format("Could act on %s", l.toString()));
  }

  public CCSTransitionException(Process p, String s) {
    super(String.format("Process %s failed to act: %s", p.represent(), s));
  }
}
