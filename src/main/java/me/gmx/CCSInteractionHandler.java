package me.gmx;

import me.gmx.process.ProcessTemplate;
import me.gmx.process.nodes.*;
import me.gmx.util.SetUtil;

import java.util.*;

public class CCSInteractionHandler {

    public ProcessTemplate template;
    public CCSInteractionHandler(ProcessTemplate template){
        this.template = template;
    }


    public boolean startInteraction(){
        Scanner scan = new Scanner(System.in);
        while(true){
            ArrayList<Label> actionable = new ArrayList<Label>(template.getActionableLabels());
            if (actionable.isEmpty())
                break;

            System.out.println("------| Actionable Labels |------");
            //Print out labels
            int i = 0;
            for (Label na : actionable)
                System.out.printf("[%d] %s%n",i++,na.origin());
            System.out.println("------------");
            System.out.println(String.format("%s", template.prettyString()));
            System.out.println("Please input the index of the label you'd like to act on:");
            String st = scan.next();
            Label n;
            if (st == "") continue;
            try{
                int in = Integer.parseInt(st);
                n = (Label) actionable.get(in);
            } catch(Exception e){
                System.out.println("Failed to recognize label!");
                continue;
            }

            try{
                System.out.println(String.format("%s -%s-> %s",
                        template.prettyString(),n.origin(),template.actOn(n).prettyString()));

            }catch (Exception e){
                System.out.println("Could not act on label!");
                if (RCCS.DEBUG) e.printStackTrace();
            }




        }

        return true;
    }


}
