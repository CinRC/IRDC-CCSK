package org.cinrc.process.process;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.cinrc.IRDC;
import org.cinrc.process.nodes.Label;
import org.cinrc.process.nodes.LabelKey;
import org.cinrc.util.RCCSFlag;

public class NullProcess extends Process {

  private String facade;

  public NullProcess() {
    super();
  }

  public NullProcess(String facade){ //TODO: Unify constructors, change all references to give 0 as facade
    super();
    this.facade = facade;
  }

  public NullProcess(String facade, List<Label> prefixes){
    super();
    this.facade = facade;
    this.prefixes.addAll(prefixes);
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
    p.facade = this.facade;
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
    if (IRDC.config.contains(RCCSFlag.DISPLAY_NULL) || prefixes.isEmpty()) {
      String s = facade == null ? "0" : facade;
      return super.represent(s);
    }

    return super.represent("");
  }

  @Override
  public Collection<Label> getActionableLabels() {
    return super.getActionableLabels();
  }

  @Override
  public Collection<Process> getChildren() {
    return Collections.emptySet();
  }

  @Override
  public String origin() {
    return facade == null ? "0" : facade;
  }
}
