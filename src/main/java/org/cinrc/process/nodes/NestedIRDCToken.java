package org.cinrc.process.nodes;

import java.util.List;

public class NestedIRDCToken extends KnownIRDCToken{
  private final List<KnownIRDCToken> tokens;
  public NestedIRDCToken(List<KnownIRDCToken> tokens){
    super("<Nested Object>", null);
    this.tokens = tokens;
  }

  public List<KnownIRDCToken> getTokens(){
    return tokens;
  }

  public String represent(){
    StringBuilder sb = new StringBuilder();
    for (KnownIRDCToken token : tokens) {
      if (!(token instanceof NestedIRDCToken)){
        sb.append(token.represent());
        continue;
      }
      sb.append("(");
      sb.append(token.represent());
      sb.append(")");
    }
    return sb.toString();
  }

  public void addToken(KnownIRDCToken token){
    this.tokens.add(token);
  }

}
