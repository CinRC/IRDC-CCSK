package org.cinrc.process.nodes;

import org.cinrc.parser.TestCCSGrammar;
import org.cinrc.process.process.ConcurrentProcess;

public class KnownIRDCToken extends IRDCToken{
  protected TestCCSGrammar tokenGrammar;
  protected IRDCObject data;

  public KnownIRDCToken(String representation, TestCCSGrammar tokenGrammar){
    super(representation);
    this.tokenGrammar = tokenGrammar;
  }

  public TestCCSGrammar getGrammar(){
    return tokenGrammar;
  }

}
