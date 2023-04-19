package org.cinrc.process.nodes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.cinrc.parser.CCSParser;
import org.cinrc.parser.CCSParserException;
import org.cinrc.parser.TestCCSGrammar;

public class NestedIRDCToken extends KnownIRDCToken{
  private List<KnownIRDCToken> tokens;
  public NestedIRDCToken(List<KnownIRDCToken> tokens){
    super("<Nested Object>", null);
    this.tokens = tokens;
  }

  public List<KnownIRDCToken> getTokens(){
    return tokens;
  }

  public String represent(){
    String s = "";
    for (KnownIRDCToken token : tokens) {
      if (!(token instanceof NestedIRDCToken)){
        s += token.represent();
        continue;
      }
      s += "(";
      s += token.represent();
      s += ")";
    }
    return s;
  }

  public void handleLabels(){
    List<Label> labels = new ArrayList<>();
    for (KnownIRDCToken token : getTokens()){
      if (token.getGrammar() == TestCCSGrammar.LABEL_ANY){
        labels.add(LabelFactory.parseNode(token.represent()));
      }
    }
  }

  public void addToken(KnownIRDCToken token){
    this.tokens.add(token);
  }

}
