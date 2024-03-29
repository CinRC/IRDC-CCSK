package org.cinrc.process.nodes;

import org.cinrc.parser.ProcessBuilder;

public abstract class IRDCToken {
  private final String representation;
  public IRDCToken(String representation){
    this.representation = representation;
  }

  public String represent(){
    return representation;
  }

  public String toString(){
    return represent();
  }

}
