package org.cinrc.process;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import org.cinrc.parser.CCSGrammar;
import org.cinrc.parser.CCSParserException;
import org.cinrc.process.nodes.Label;
import org.cinrc.process.process.ComplexProcess;
import org.cinrc.process.process.Process;


public class ProcessTemplate {
  private final LinkedList<Process> tList;
  public boolean isInit = false;

  public ProcessTemplate() {
    tList = new LinkedList<>();
  }

  public void add(Process node) {
    tList.add(node);
  }

  /**
   * Initialize the template into a working process. This method will cycle through all complex
   * processes, consuming simple processes to make up each part. Complex processes
   * are processed through this pipeline in the order in CCSGrammar
   */
  public void initComplex() {
    //Collect complex processes
    LinkedHashSet<ComplexProcess> complex = new LinkedHashSet<>();
    for (Process process : tList) {
      if (process instanceof ComplexProcess) {
        complex.add((ComplexProcess) process);
      }
    }


    //We rely on descending binding order in the CCSGrammar class
    for (CCSGrammar g : CCSGrammar.values()) {
      for (ComplexProcess p : complex) {
        if (p.getClass() == g.getClassObject()) {
          if (p.left == null)
          //Consume object to the left
          {
            int i = tList.indexOf(p) - 1;
            p.left = tList.remove(tList.indexOf(p) - 1);
          }
          if (p.right == null)
          //Consume object to the right
          {
            p.right = tList.remove(tList.indexOf(p) + 1);
          }
        }
      }
    }
    isInit = true;
  }

  /**
   * Exports ProcessTemplate as a process. ProcessTemplate should always init down to
   * a single 'parent' process.
   *
   * @return Parent process
   * @throws CCSParserException If for some reason there are more than 1 parent processes
   */
  public Process export() {
    if (!isInit) {
      initComplex();
    }
    if (tList.size() != 1) {
      throw new CCSParserException("Could not export process template into process!");
    } else {
      return tList.get(0);
    }
  }

  /**
   * Adds restrictions along given labels to the last item in the template array
   *
   * @param restrictions Array of labels to restrict along
   */
  public void addRestrictionToLastProcess(Collection<Label> restrictions) {
    this.tList.getLast().addRestrictions(restrictions);
  }

  public String prettyString() {
    StringBuilder sb = new StringBuilder();
    for (Process p : tList) {
      sb.append(p.represent());
    }
    return sb.toString();
  }

  public LinkedList<Process> getProcesses() {
    return this.tList;
  }

  public void prependTemplate(ProcessTemplate t) {
    t.getProcesses().addAll(tList);
  }

  public void appendTemplate(ProcessTemplate t) {
    tList.addAll(t.getProcesses());
  }


}
