package org.cinrc.process.nodes;

import org.cinrc.parser.CCSGrammar;

public class KnownIRDCToken extends IRDCToken{
  protected CCSGrammar tokenGrammar;
  protected IRDCObject data;

  public KnownIRDCToken(String representation, CCSGrammar tokenGrammar){
    super(representation);
    this.tokenGrammar = tokenGrammar;
  }

  public IRDCObject getData(){
    return data;
  }

  public void setData(IRDCObject o){
    this.data = o;
  }

  public CCSGrammar getGrammar(){
    return tokenGrammar;
  }

}
