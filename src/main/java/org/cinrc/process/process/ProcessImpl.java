package org.cinrc.process.process;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.cinrc.process.nodes.Label;
import org.cinrc.process.nodes.LabelKey;

public class ProcessImpl extends Process implements ActionableProcess {

  public ProcessImpl(String s) {
    super();
    this.origin = s;
  }

  public ProcessImpl(String s, List<Label> prefixes) {
    super();
    this.origin = s;
    this.prefixes.addAll(prefixes);
  }

  public Process attemptRewind(LabelKey key) {
    return previousLife;
  }

  @Override
  public Process clone() {
    ProcessImpl p = new ProcessImpl(origin());
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
    return new NullProcess();
  }

  public String represent() {
    return super.represent(origin());
  }

  @Override
  public Collection<Process> getChildren() {
    return Collections.emptySet();
  }

  @Override
  public Collection<Label> getActionableLabels() {
    return annotateRestrictions(super.getActionableLabels());
  }

  @Override
  public String origin() {
    return origin;
  }

}
