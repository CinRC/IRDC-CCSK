package org.cinrc;

import java.util.ArrayList;
import java.util.Scanner;
import org.cinrc.parser.CCSTransitionException;
import org.cinrc.process.ProcessContainer;
import org.cinrc.process.nodes.Label;
import org.cinrc.process.nodes.LabelKey;
import org.cinrc.process.process.Process;
import org.cinrc.util.RCCSFlag;

public class CCSInteractionHandler {

  private ProcessContainer container;

  public CCSInteractionHandler(Process p) {

    this.container = new ProcessContainer(p);
  }

  public void setProcess(Process p) {
    this.container = new ProcessContainer(p);
  }

  public ProcessContainer getContainer() {
    return container;
  }

  public boolean actOn(int labelIndex) {
    try {
      container.act(getActionableLabels().get(labelIndex));
      return true;
    } catch (CCSTransitionException e) {
      e.printStackTrace();
      return false;
    }
  }

  public String getProcessRepresentation() {
    return container.prettyString();
  }

  public ArrayList<Label> getActionableLabels() {
    return new ArrayList<Label>(container.getActionableLabels());
  }

  public boolean startInteraction() {
    Scanner scan = new Scanner(System.in);
    while (true) {
      ArrayList<Label> actionable = getActionableLabels();
      if (actionable.isEmpty()) {
        break;
      }

      System.out.println("------| Actionable Labels |------");
      //Print out labels
      int i = 0;
      for (Label na : actionable) {
        System.out.printf("[%d] %s%n", i++, na);
      }
      System.out.println("[q] Quit program");
      System.out.println("------------");
      System.out.printf("%s%n", container.prettyString());
      System.out.println("Please input the index of the label you'd like to act on:");
      String st = scan.next();
      Label n;
      if (st.equals("")) {
        continue;
      } else if (st.equalsIgnoreCase("q")) {
        return false;
      }
      try {
        int in = Integer.parseInt(st);
        n = actionable.get(in);
      } catch (Exception e) {
        System.out.println("Failed to recognize label!");
        continue;
      }

      String past = container.prettyString();
      try {
        container.act(n);
      } catch (Exception e) {
        System.out.println("Could not act on label!");
        if (IRDC.config.contains(RCCSFlag.DEBUG)) {
          e.printStackTrace();
        }
      }
      String c = n instanceof LabelKey ? "~" : "-";
      System.out.printf("%s %s%s%s> %s%n",
          past, c, n, c, container.prettyString());
    }

    return true;
  }


}
