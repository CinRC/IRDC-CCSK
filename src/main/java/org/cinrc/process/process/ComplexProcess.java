package org.cinrc.process.process;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Set;
import org.cinrc.IRDC;
import org.cinrc.parser.CCSGrammar;
import org.cinrc.process.nodes.Label;
import org.cinrc.util.RCCSFlag;
import org.cinrc.util.SetUtil;

/**
 * Complex process class is a process that is represented by two processes
 * linked together by an operator
 */
public abstract class ComplexProcess extends Process {

  public Process left;

  public Process right;
  CCSGrammar operator;


  public ComplexProcess(Process left, Process right, CCSGrammar operator) {
    this.left = left;
    this.right = right;
    this.operator = operator;
    this.origin = origin();
    this.displayKey = true;
  }

  /**
   * Returns a non-recursive sub-process
   */
  @Override
  public Collection<Process> getChildren() {
    return Set.of(left, right);
  }


  /**
   * Checks if the process has been packed
   */
  public boolean isPacked() {
    return !(left == null || right == null);
  }

  /**
   * "Packs" processes to the left and right of this process to form
   * a packed complex process.
   *
   * @param template ordered list of processes representing a formula that the process will pack from.
   * @return leftover processes that were not packed
   */
  public LinkedList<Process> pack(LinkedList<Process> template) {
    for (int i = 0; i < template.size(); i++) {

      if (template.get(i) == this) {
        if (left == null) {
          IRDC.log(String.format("Using %s to init left ", template.get(i - 1).origin()));
          left = template.remove(i - 1);
          i--;
        }
        if (right == null) {
          IRDC.log(
              String.format("Using %s to init right ", template.get(i + 1).origin()));
          if (template.size() > i + 1) {
            right = template.remove(i + 1);
          }

        }
      }
    }

    return template;
  }

  protected abstract Collection<Label> getActionableLabelsStrict();

  @Override
  public String represent() {
    String s = "";
    if (ghostKey != null) {
      if (IRDC.config.contains(RCCSFlag.SUMMATION_STYLE_1)) {
        s = represent(String.format(
            "%s%s%s%s%s",
            CCSGrammar.OPEN_PARENTHESIS,
            left == null ? "" : left.represent(),
            operator.toString(),
            right == null ? "" : right.represent(),
            CCSGrammar.CLOSE_PARENTHESIS
        ));
      } else if (IRDC.config.contains(RCCSFlag.SUMMATION_STYLE_3)) {
        if (left.isGhost) {
          s = represent(String.format(
              "%s",
              right == null ? "" : right.represent()
          ));
        } else if (right.isGhost) {
          s = represent(String.format(
              "%s",
              left == null ? "" : left.represent()
          ));
        }
      } else { //default style
        if (left.isGhost) {
          s = represent(String.format(
              "%s{%s} %s %s%s%s",
              ghostKey,
              left == null ? "" : left.represent(),
              operator.toString(),
              CCSGrammar.OPEN_PARENTHESIS,
              right == null ? "" : right.represent(),
              CCSGrammar.CLOSE_PARENTHESIS
          ));
        } else {
          s = represent(String.format(
              "%s%s%s %s %s{%s}",
              CCSGrammar.OPEN_PARENTHESIS,
              left == null ? "" : left.represent(),
              CCSGrammar.CLOSE_PARENTHESIS,
              operator.toString(),
              ghostKey,
              right == null ? "" : right.represent()
          ));
        }
      }
    }
    s = super.represent(String.format(
        "%s%s%s%s%s",
        CCSGrammar.OPEN_PARENTHESIS,
        left == null ? "" : left.represent(),
        operator.toString(),
        right == null ? "" : right.represent(),
        CCSGrammar.CLOSE_PARENTHESIS
    ));

    if (IRDC.config.contains(RCCSFlag.HIDE_PARENTHESIS)) {
      return s.replaceAll(String.format("\\%s", CCSGrammar.OPEN_PARENTHESIS), "")
          .replaceAll(String.format("\\%s", CCSGrammar.CLOSE_PARENTHESIS), "");

    } else {
      return s;
    }

  }

  /**
   * Returns the 'debug' form of human readable representation.
   *
   * @return Internal human readable format of what the process should look like
   */
  @Override
  public String origin() {
    StringBuilder b = new StringBuilder();
    if (!IRDC.config.contains(RCCSFlag.HIDE_PARENTHESIS)) {
      b.append(CCSGrammar.OPEN_PARENTHESIS);
    }
    if (left != null) {
      b.append(left.origin());
    }
    b.append(operator);
    if (right != null) {
      b.append(right.origin());
    }
    if (!IRDC.config.contains(RCCSFlag.HIDE_PARENTHESIS)) {
      b.append(CCSGrammar.CLOSE_PARENTHESIS);
    }

    return b.toString();
  }

  public boolean hasKey() {
    if (left == null || right == null) {
      return false;
    } else {
      return left.hasKey() || right.hasKey();
    }
  }

  public abstract ComplexProcess clone();

  @Override
  public boolean canAct(Label label) {
    IRDC.log(String.format("Checking if %s can act on %s", represent(), label));
    Collection<Label> l = getActionableLabels();
    return SetUtil.containsOrTau(l, label);

  }

  protected Collection<Label> getLeftRightLabels() {
    Collection<Label> l = left.getActionableLabels();
    l.addAll(right.getActionableLabels());
    return l;
  }


}
