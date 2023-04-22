package org.cinrc.parser;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import org.cinrc.IRDC;
import org.cinrc.process.nodes.Label;
import org.cinrc.process.nodes.LabelKey;
import org.cinrc.process.nodes.TauLabelNode;
import org.cinrc.process.process.NullProcess;
import org.cinrc.process.process.Process;
import org.cinrc.process.process.ProcessImpl;
import org.cinrc.util.RCCSFlag;

public class CCSParser {

  HashMap<Integer, LabelKey> tauMap;
  int counter;
  boolean inParenthesis, inKeyNotation, inSetNotation;
  LinkedList<Label> prefixes, restrictions;
  LinkedList<LabelKey> keys;

  Collection<RCCSFlag> config;
  public CCSParser() {
    tauMap = new HashMap<Integer, LabelKey>();
    config = new HashSet<>(IRDC.config);
    counter = 0;
    inParenthesis = false;
    inKeyNotation = false;
    inSetNotation = false;
    prefixes = new LinkedList<>();
    restrictions = new LinkedList<>();
    keys = new LinkedList<>();
  }

  public CCSParser(HashMap<Integer, LabelKey> tauMap){
    CCSParser p = new CCSParser();
    p.tauMap = tauMap;
  }


  public Process parseLine(String s ){
    return new ProcessBuilder(s).export();
  }



  public static LinkedList<Label> parseLabelsFromKeySet(LinkedList<LabelKey> keys){
    LinkedList<Label> l = new LinkedList<>();
    for (LabelKey k : keys){
      if (k.from instanceof TauLabelNode tau){
       if (!tau.consumeLeft){
         l.add(tau.getA());
       }else if (!tau.consumeRight){
         l.add(tau.getB());
       }
      }else {
        l.add(k.from);
      }
    }
    return l;
  }

  public static Process generateProcess(String s, LinkedList<Label> prefixes, LinkedList<LabelKey> keys){
    Process p = null;
    if (CCSGrammar.PROC_NUL.match(s).matches()){
      p = new NullProcess();
    }else if (CCSGrammar.PROC_NAM.match(s).matches()){
      p = new ProcessImpl(s);
    }
    return generateProcess(p, prefixes, keys);
  }


  public static Process generateProcess(Process p, LinkedList<Label> prefixes, LinkedList<LabelKey> keys){

    LinkedList<Label> parsedPrefixes = CCSParser.parseLabelsFromKeySet(keys);
    parsedPrefixes.addAll(prefixes);
    p.addPrefixes(parsedPrefixes);

    for (LabelKey key : keys){
      /*if (key.from instanceof TauLabelNode t){
        if (!t.consumeLeft){
          p.act(t.getA());
          t.consumeLeft = true;
        }else if (!t.consumeRight){
          p.act(t.getB());
          t.consumeRight = true;
        }else{
          throw new CCSParserException("Something went wrong when trying to manage tau!");
        }
      }else */{
        p.act(key.from);
      }
    }

    prefixes.clear();
    keys.clear();
    return p;
  }



}
