package me.gmx;

import me.gmx.process.ProcessContainer;
import me.gmx.process.nodes.Label;
import me.gmx.process.nodes.LabelKey;
import me.gmx.util.RCCSFlag;

import java.util.ArrayList;
import java.util.Scanner;

public class CCSInteractionHandler {

    private ProcessContainer container;

    public CCSInteractionHandler(ProcessContainer container) {
        this.container = container;
    }


    public ProcessContainer getContainer() {
        return container;
    }

    public boolean startInteraction() {
        Scanner scan = new Scanner(System.in);
        while (true) {
            ArrayList<Label> actionable = new ArrayList<Label>(container.getActionableLabels());
            if (actionable.isEmpty())
                break;

            System.out.println("------| Actionable Labels |------");
            //Print out labels
            int i = 0;
            for (Label na : actionable)
                System.out.printf("[%d] %s%n", i++, na);
            System.out.println("------------");
            System.out.printf("%s%n", container.prettyString());
            System.out.println("Please input the index of the label you'd like to act on:");
            String st = scan.next();
            Label n;
            if (st.equals("")) continue;
            else if (st.equalsIgnoreCase("q")){
                return false;
            }
            try{
                int in = Integer.parseInt(st);
                n = (Label) actionable.get(in);
            } catch(Exception e){
                System.out.println("Failed to recognize label!");
                continue;
            }

            String past = container.prettyString();
            try{
                container.act(n);
            }catch (Exception e){
                System.out.println("Could not act on label!");
                if (RCCS.config.contains(RCCSFlag.DEBUG)) e.printStackTrace();
            }
            String c = n instanceof LabelKey ? "~" : "-";
            System.out.printf("%s %s%s%s> %s%n",
                    past,c,n,c,container.prettyString());
        }

        return true;
    }


}
