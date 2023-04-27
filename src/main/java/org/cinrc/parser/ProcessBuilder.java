package org.cinrc.parser;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.stream.Collectors;
import org.cinrc.IRDC;
import org.cinrc.process.ProcessTemplate;
import org.cinrc.process.nodes.IRDCToken;
import org.cinrc.process.nodes.KnownIRDCToken;
import org.cinrc.process.nodes.Label;
import org.cinrc.process.nodes.LabelFactory;
import org.cinrc.process.nodes.LabelKey;
import org.cinrc.process.nodes.LabelNode;
import org.cinrc.process.nodes.NestedIRDCToken;
import org.cinrc.process.nodes.NodeIDGenerator;
import org.cinrc.process.nodes.TauLabelNode;
import org.cinrc.process.nodes.UnknownIRDCToken;
import org.cinrc.process.process.ConcurrentProcess;
import org.cinrc.process.process.Process;
import org.cinrc.process.process.SummationProcess;
import org.cinrc.util.RCCSFlag;
import org.cinrc.util.SetUtil;

public class ProcessBuilder {

  public List<IRDCToken> template;
  public boolean isTokenized = false;

  private final List<LabelKey> taus;

  private NestedIRDCToken parent;

  public ProcessBuilder(String base){
    template = new ArrayList<>();
    insert(new UnknownIRDCToken(base), 0);
    NodeIDGenerator.reset();
    taus = new ArrayList<>();//TODO: matching
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
    StringBuilder sb = new StringBuilder();
    for (KnownIRDCToken token : knownTokens)
      sb.append(token.represent());
    IRDC.log("Recursing into " + sb);


    boolean inPar = false;
    for (KnownIRDCToken token : knownTokens){ //for every token
      if (token.getGrammar() == CCSGrammar.OPEN_PAR){ //if open parenthesis, increment counter
        counter++;
        inPar = true;
      }else if (token.getGrammar() == CCSGrammar.CLOSE_PAR){// if close parenthesis, decrement counter
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
    return export(parent);
  }
  private Process export(NestedIRDCToken nest){
    return export(nest, null);
  }
  private Process export(NestedIRDCToken nest, LinkedList<LabelKey> key){
    LinkedList<LabelKey> keys = key == null ? new LinkedList<>() : key;
    LinkedList<Label> prefixes = null == null ? new LinkedList<>() : null;

    ProcessTemplate template = new ProcessTemplate();
    for (KnownIRDCToken token : nest.getTokens()){
      if (token instanceof NestedIRDCToken n){
        template.add(CCSParser.generateProcess(export(n),prefixes,keys));
        continue;
      }
      List<KnownIRDCToken> nestList = nest.getTokens();
      int index = nestList.indexOf(token);
      switch(token.getGrammar()){
        case LABEL_ANY:
          prefixes.add(LabelFactory.parseNode(token.represent()));
          determineNextSequence(template, token, nestList, index, keys, prefixes);
          break;
        case LABEL_KEY:
          if (!prefixes.isEmpty())
            throw new CCSParserException("Process is unreachable! Keys cannot be prefixed");

          LabelKey k = ((LabelKey) LabelFactory.parseNode(token.represent()));
          if (k.from instanceof TauLabelNode t) {
            if (taus.isEmpty()) {
              taus.add(k);
              keys.add(k);
            } else {
              for (LabelKey tt : taus) {
                if (tt.dupe == k.dupe) { //same channel
                  if (!tt.from.getChannel().equals(t.getChannel())){
                    throw new CCSParserException("Found two tau keys with identical keys but separate channels! " +
                        SetUtil.csvSet(Set.of(tt,k)));
                  }
                  keys.add(tt);
                  taus.remove(tt);
                  break;
                }
              }
            }
          }

          determineNextSequence(template, token, nestList, index, keys, prefixes);
          break;
        case OP_PAR:
          template.add(new ConcurrentProcess(null, null));
          break;
        case OP_SUM:
          template.add(new SummationProcess(null, null));
          break;
        case OP_SEQ:
          break; //do nothing
        case RESTRICTION:
          template.addRestrictionToLastProcess(parseLabelsFromRestriction(token.represent()));
          break;
        case PROC_NUL:
        case PROC_NAM:
          template.add(CCSParser.generateProcess(token.represent(),prefixes,keys));
          break;
      }
    }
    template.initComplex();
    if (!taus.isEmpty()){
      throw new CCSParserException("Unmatched tau keys! " + SetUtil.csvSet(taus));
    }
    return template.export();
  }

  private void determineNextSequence(ProcessTemplate template, KnownIRDCToken token,
                                     List<KnownIRDCToken> nestList, int index,
                                     LinkedList<LabelKey> keys, LinkedList<Label> prefixes) {
    if (index+1 == nestList.size()
        || nestList.get(index+1).getGrammar() != CCSGrammar.OP_SEQ){
      if (IRDC.config.contains(RCCSFlag.REQUIRE_EXPLICIT_NULL)){
        throw new CCSParserException("Did not find a process after label " + token.represent());
      }
      template.add(CCSParser.generateProcess("0",prefixes, keys));
    }
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
    for (CCSGrammar grammar : CCSGrammar.sort()){
      for (IRDCToken token : getUnkownTokens()){
        List<IRDCToken> tok = tokenize(grammar, token);
        int index = template.indexOf(token);
        template.remove(index);
        template.addAll(index, tok);
      }
    }
    if (template.stream().anyMatch(token -> token instanceof UnknownIRDCToken)){
      StringBuilder sb = new StringBuilder();
      for (IRDCToken ta : template){
        if (ta instanceof UnknownIRDCToken){
          sb.append(ta.represent());
          sb.append(",");
        }
      }
      throw new CCSParserException("Attempted to parse " + initial + " but was left with unrecognized characters: " +
          "\n " + sb);
    }
    isTokenized = true;
  }

  private List<IRDCToken> tokenize(CCSGrammar grammar, IRDCToken token){
    ArrayList<IRDCToken> tok = new ArrayList<>();
    Matcher m = grammar.match(token.represent());
    if (!m.find()) {
      return List.of(token);
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
  public List<Label> parseLabelsFromRestriction(String st){
    List<Label> labels = new ArrayList<>();
    Matcher m = CCSGrammar.LABEL_IN.match(st);
    while (m.find()){
      labels.add(LabelFactory.parseNode(m.group()));
    }
    if (labels.stream().noneMatch(LabelNode.class::isInstance)){
      throw new CCSParserException("Found an unexpected token inside restriction: " + st);
    }
    return labels;
  }


}
