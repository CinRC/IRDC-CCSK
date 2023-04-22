package org.cinrc.parser;

import java.lang.reflect.AnnotatedArrayType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.stream.Collectors;
import org.cinrc.process.ProcessTemplate;
import org.cinrc.process.nodes.IRDCObject;
import org.cinrc.process.nodes.IRDCToken;
import org.cinrc.process.nodes.KnownIRDCToken;
import org.cinrc.process.nodes.Label;
import org.cinrc.process.nodes.LabelFactory;
import org.cinrc.process.nodes.LabelKey;
import org.cinrc.process.nodes.LabelNode;
import org.cinrc.process.nodes.NestedIRDCToken;
import org.cinrc.process.nodes.RestrictionPlaceholder;
import org.cinrc.process.nodes.TauLabelNode;
import org.cinrc.process.nodes.UnknownIRDCToken;
import org.cinrc.process.process.ConcurrentProcess;
import org.cinrc.process.process.NullProcess;
import org.cinrc.process.process.Process;
import org.cinrc.process.process.ProcessImpl;
import org.cinrc.process.process.SummationProcess;

public class ProcessBuilder {

  public List<IRDCToken> template;
  public boolean isTokenized = false;

  private LinkedList<Label> prefixes;

  private LinkedList<LabelKey> keys;

  private List<TauLabelNode> taus;

  private NestedIRDCToken parent;

  public ProcessBuilder(String base){
    template = new ArrayList<>();
    insert(new UnknownIRDCToken(base), 0);
    prefixes = new LinkedList<>();
    keys = new LinkedList<>();
    taus = new LinkedList<>();
  }

  public void handleParentheses(){
    if (!isTokenized)
      throw new CCSParserException("Attempted to group by parenthesis whilst not tokenized!");
    parent = handleParentheses(getKnownTokens());
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

  public Process export(){ // process;
    tokenize();
    handleParentheses();

    return null;
  }

  private Process export(List<KnownIRDCToken> tokens){

    while(!tokens.isEmpty()){
      KnownIRDCToken token = tokens.remove(0);

      if (token instanceof NestedIRDCToken nest){
        return export(nest.getTokens());
      }

      switch(token.getGrammar()){
        case LABEL_ANY:
          prefixes.add((Label) token.getData());
        case LABEL_KEY:
          keys.add((LabelKey) token.getData());
        case PROC_NAM:



      }

    }
  }


  public KnownIRDCToken flatten(NestedIRDCToken nest){
    for (KnownIRDCToken token : nest.getTokens()){
      if (token.getData() == null) {
        initializeToken(token);
      }


    }
  }

  private KnownIRDCToken initializeToken(KnownIRDCToken token){
    if (token.getData() != null){
      throw new CCSParserException("Tried to process token " + token.represent()
          + " , but found pre-existing data."); //not needed. just want to know when it happens
    }else if (token instanceof NestedIRDCToken nest){
      for (KnownIRDCToken inside : nest.getTokens()){
        initializeToken(inside);
      }//all members of nest are initialized
      KnownIRDCToken t = new KnownIRDCToken(nest.represent(), null);
      t.setData(combine(nest));
      return t;

    }
    switch (token.getGrammar()){
      case LABEL_KEY:
      case LABEL_ANY:
        token.setData(LabelFactory.parseNode(token.represent()));
        break;
      case OP_PAR:
        token.setData(new ConcurrentProcess(null, null));
        break;
      case OP_SUM:
        token.setData(new SummationProcess(null, null));
        break;
      case PROC_NAM:
        token.setData(new ProcessImpl(token.represent()));
        break;
      case PROC_NUL:
        token.setData(new NullProcess());
        break;
      case OP_SEQ:
        break;//do nothing
      case RESTRICTION:
        token.setData(parseLabelsFromRestriction(token.represent()));
      default:
        throw new CCSParserException("Could not process token " + token.represent());

    }
    return token;
  }


  public void insert(IRDCToken token, int index){
    template.add(index, token);
  }

  public IRDCToken getIndex(int index){
    return template.get(index);
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

  //TODO: Improve-- check if unmatch
  public RestrictionPlaceholder parseLabelsFromRestriction(String st){
    List<Label> labels = new ArrayList<>();
    Matcher m = TestCCSGrammar.LABEL_IN.match(st);
    while (m.find()){
      labels.add(LabelFactory.parseNode(m.group()));
    }
    if (labels.stream().noneMatch(LabelNode.class::isInstance)){
      throw new CCSParserException("Found an unexpected token inside restriction: " + st);
    }
    return new RestrictionPlaceholder(labels);
  }


}
