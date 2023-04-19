package org.cinrc.parser;

import java.lang.reflect.AnnotatedArrayType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.stream.Collectors;
import org.cinrc.process.ProcessTemplate;
import org.cinrc.process.nodes.IRDCToken;
import org.cinrc.process.nodes.KnownIRDCToken;
import org.cinrc.process.nodes.Label;
import org.cinrc.process.nodes.LabelFactory;
import org.cinrc.process.nodes.NestedIRDCToken;
import org.cinrc.process.nodes.UnknownIRDCToken;
import org.cinrc.process.process.Process;

public class ProcessBuilder {

  public List<IRDCToken> template;
  public boolean isTokenized = false;

  public ProcessBuilder(String base){
    template = new ArrayList<>();
    insert(new UnknownIRDCToken(base), 0);
  }

  public List<List<KnownIRDCToken>> groupByParentheses(){
    if (!isTokenized)
      throw new CCSParserException("Attempted to group by parenthesis whilst not tokenized!");

    List<List<KnownIRDCToken>> a = groupByParentheses(getKnownTokens());
    return a;
  }


  public NestedIRDCToken handleParentheses(){
    if (!isTokenized)
      throw new CCSParserException("Attempted to group by parenthesis whilst not tokenized!");

    NestedIRDCToken a = handleParentheses(getKnownTokens());
    return a;
  }

  private List<List<KnownIRDCToken>> groupByParentheses(List<KnownIRDCToken> knownTokens){
    int counter = 0;
    List<List<KnownIRDCToken>> oa = new ArrayList<>();
    List<KnownIRDCToken> tmp = new ArrayList<>();
    boolean inPar = false;
    for (KnownIRDCToken token : knownTokens){ //for every token
      if (token.getGrammar() == TestCCSGrammar.OPEN_PAR){ //if open parenthesis, increment counter
        counter++;
        //Starting first parenthesis? dump tmp
        if (!inPar){
          for (KnownIRDCToken t : tmp)
            oa.add(Arrays.asList(t));
          tmp.clear();
        }
        inPar = true;
      }else if (token.getGrammar() == TestCCSGrammar.CLOSE_PAR){// if close parenthesis, decrement counter
        counter--;
        if (counter < 0)
          throw new CCSParserException("Mismatched parentheses!");
      }
      tmp.add(token); //add token to tmp list of tokens

      if (counter == 0 && inPar){ //If master paren is closing
        tmp.remove(tmp.size()-1);
        tmp.remove(0);
        //remove prefix and suffix paren
        oa.addAll(groupByParentheses(tmp)); //recurse on whats left
        tmp.clear(); //reset and continue
        inPar = false;
      }
    }
    if (!tmp.isEmpty())
      oa.add(tmp);
    if (counter != 0)
      throw new CCSParserException("Mismatched parentheses!");
    return oa;
  }

  private NestedIRDCToken handleParentheses(List<KnownIRDCToken> knownTokens){
    int counter = 0;
    List<KnownIRDCToken> tmp = new ArrayList<>();
    NestedIRDCToken oa = new NestedIRDCToken(new ArrayList<>());
    String s = "";
    for (KnownIRDCToken token : knownTokens)
      s+=token.represent();
    System.out.println("Recursing into " + s);


    boolean inPar = false;
    for (KnownIRDCToken token : knownTokens){ //for every token
      if (token.getGrammar() == TestCCSGrammar.OPEN_PAR){ //if open parenthesis, increment counter
        counter++;
        inPar = true;

      }else if (token.getGrammar() == TestCCSGrammar.CLOSE_PAR){// if close parenthesis, decrement counter
        counter--;
        if (counter < 0)
          throw new CCSParserException("Mismatched parentheses!");
      }
      if (!inPar)
        oa.addToken(token);
      else
        tmp.add(token); //add token to tmp list of tokens

      if (counter == 0 && inPar){ // end par
        tmp.remove(0);
        tmp.remove(tmp.size()-1); //strip
        oa.addToken(handleParentheses(tmp));
        tmp.clear();
        inPar = false;
      }
    }
    if (counter != 0)
      throw new CCSParserException("Mismatched parentheses!");
    return oa;
  }


  public void insert(IRDCToken token, int index){
    template.add(index, token);
  }

  public IRDCToken getIndex(int index){
    return template.get(index);
  }

  public Process export(){
    return null;
  }

  public int indexOf(IRDCToken token){
    return template.indexOf(token);
  }

  public void tokenize(){
    String initial = template.get(0).represent();
    for (TestCCSGrammar grammar : TestCCSGrammar.sort()){
      for (IRDCToken token : getUnkownTokens()){
        List<IRDCToken> tok = tokenize(grammar, token);
        int index = template.indexOf(token);
        template.remove(index);
        template.addAll(index, tok);
      }
    }
    if (template.stream().anyMatch(token -> token instanceof UnknownIRDCToken)){
      String error = "";
      for (IRDCToken ta : template){
        if (ta instanceof UnknownIRDCToken){
          error += ta.represent() + ",";
        }
      }
      throw new CCSParserException("Attempted to parse " + initial + " but was left with unrecognized characters: " +
          "\n " + error);
    }
    isTokenized = true;
  }

  private List<IRDCToken> tokenize(TestCCSGrammar grammar, IRDCToken token){
    ArrayList<IRDCToken> tok = new ArrayList<>();
    Matcher m = grammar.match(token.represent());
    if (!m.find())
      return Arrays.asList(token);
    else {
    }
      String[] split = token.represent().split(grammar.pString, 2);
      String a = split[0];
      KnownIRDCToken b = new KnownIRDCToken(m.group(), grammar);
      String c = split[1];
      if (!a.isBlank()) {
        tok.addAll(tokenize(grammar, new UnknownIRDCToken(a)));
      }
      tok.add(b);
      if (!c.isBlank()){
        tok.addAll(tokenize(grammar,new UnknownIRDCToken(c)));
      }
      return tok;
  }



  public List<UnknownIRDCToken> getUnkownTokens(){
    return new ArrayList<>(template)
        .stream().filter(token -> token instanceof UnknownIRDCToken)
        .map(token -> (UnknownIRDCToken) token)
        .collect(Collectors.toList());
  }

  public List<KnownIRDCToken> getKnownTokens(){
    return new ArrayList<>(template)
        .stream().filter(token -> token instanceof KnownIRDCToken)
        .map(token -> (KnownIRDCToken) token)
        .collect(Collectors.toList());
  }

  public String represent(){
    String s = "";
    for (IRDCToken token : template)
      s += token.represent();
    return s;
  }


  private void replaceToken(IRDCToken toReplace, IRDCToken replacer){
    if (!template.contains(toReplace))
      throw new CCSParserException("Tried to replace token " + toReplace + ", but could not find it");

  }


}
