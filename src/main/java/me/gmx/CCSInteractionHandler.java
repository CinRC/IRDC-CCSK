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
            Set<Label> actionable = template.getActionableLabels();
            //Find complements for tau
            Collection<TauLabelNode> tauMatches = SetUtil.getTauMatches(actionable);

            for (Label na : template.getActionableLabels()){
                System.out.println(na.origin());
            }
            for (TauLabelNode e : tauMatches){
                System.out.println(String.format(
                        "Tau{%s, %s}"
                        , e.getA().origin()
                        , e.getB().origin()
                ));
            }
            System.out.println("------------");

            System.out.println(String.format("%s", template.prettyString()));
            System.out.println("Please type the label you'd like to act on:");
            String st = scan.next();
            //a.getActionableLabels()
            if (st == "") continue;
            Label n = LabelFactory.parseNode(st);

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
