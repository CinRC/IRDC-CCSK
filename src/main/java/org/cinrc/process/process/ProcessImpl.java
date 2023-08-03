package org.cinrc.process.process;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.cinrc.parser.CCSGrammar;
import org.cinrc.process.nodes.Label;
import org.cinrc.process.nodes.LabelKey;

public class ProcessImpl extends Process implements ActionableProcess {

  public Process child;
  public ProcessImpl(String s) {
    super();
    child = new NullProcess(s);
    this.origin = s;
  }

  public ProcessImpl(Process p, List<Label> prefixes, Collection<Label> restrictions){
    super();
    this.origin = p.represent();
    this.prefixes.addAll(prefixes);
    this.addRestrictions(restrictions);
    this.child = p;
  }


  public Process attemptRewind(LabelKey key) {
    return previousLife;
  }

  @Override
  public Process clone() {
    ProcessImpl p;
    p = new ProcessImpl(this.child.clone(), getPrefixes(), getRestriction());
    if (previousLife != null) {
      p.setPastLife(previousLife.clone());
    }
    if (hasKey()) {
      p.setKey(getKey());
    }
    p.isGhost = isGhost;
    p.addRestrictions(getRestriction());
    p.addPrefixes(getPrefixes());
    return p;
  }

  //TODO: implement
  @Override
  public void execute() {

  }

  @Override
  public Process actOn(Label label) {
    return child.act(label);
  }

  public String represent() {
    return super.represent(String.format("%s", child.represent()));
  }

  @Override
  public Collection<Process> getChildren() {
    return Collections.emptySet();
  }

  @Override
  public Collection<Label> getActionableLabels() {
    if (getPrefixes().isEmpty())
      return annotateRestrictions(child.getActionableLabels());
    else
      return annotateRestrictions(super.getActionableLabels());
  }

  @Override
  public String origin() {
    return origin;
  }

}
