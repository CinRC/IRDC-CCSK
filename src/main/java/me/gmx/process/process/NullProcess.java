package me.gmx.process.process;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import me.gmx.RCCS;
import me.gmx.process.nodes.Label;
import me.gmx.process.nodes.LabelKey;
import me.gmx.util.RCCSFlag;

public class NullProcess extends Process {

  public NullProcess() {
    super();
  }

  public NullProcess(List<Label> prefixes) {
    super();
    this.prefixes.addAll(prefixes);
  }

  @Override
  public Process clone() {
    NullProcess p = new NullProcess();
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

  public Process attemptRewind(LabelKey key) {
    return previousLife;
  }

  @Override
  public Process actOn(Label label) {
    return this;
  }

  @Override
  public String represent() {
    if (RCCS.config.contains(RCCSFlag.DISPLAY_NULL) || prefixes.isEmpty()) {
      return super.represent("0");
    }

    return super.represent("");
  }

  @Override
  public Collection<Label> getActionableLabels() {
    return withdrawRestrictions(super.getActionableLabels());
  }

  @Override
  public Collection<Process> getChildren() {
    return Collections.emptySet();
  }

  @Override
  public String origin() {
    return "0";
  }
}
