package me.gmx;

import me.gmx.process.ProcessTemplate;
import me.gmx.process.nodes.*;
import me.gmx.util.SetUtil;

import java.util.*;

public class CCSInteractionHandler {

    public ProcessTemplate template;
    public Collection<RCCS.CCSFlag> flags;
    public CCSInteractionHandler(ProcessTemplate template, Collection<RCCS.CCSFlag> flags){
        this.template = template;
        this.flags = flags;
    }


    public boolean startInteraction(){
        Scanner scan = new Scanner(System.in);
        while(!template.getActionableLabels().isEmpty()){
            System.out.println("------| Actionable Labels |------");
            ArrayList<Label> actionable = new ArrayList<Label>(template.getActionableLabels());
            //Find complements for tau
            actionable.addAll(SetUtil.getTauMatches(actionable));
            //Print out labels
            int i = 0;
            for (Label na : actionable)
                System.out.printf("[%d] %s%n",i++,na.origin());

            System.out.println("------------");
            System.out.println(String.format("%s", template.prettyString()));
            /*System.out.println("Please type the label you'd like to act on:");
            String st = scan.next();

            //iNpUt VaLiDaTiOn
            if (st == "")
                continue;

            Label n = LabelFactory.parseNode(st);*/
            System.out.println("Please input the index of the label you'd like to act on:");
            String st = scan.next();
            Label n;
            if (st == "") continue;
            try{
                int in = Integer.parseInt(st);
                n = (Label) template.getActionableLabels().toArray()[in];
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
