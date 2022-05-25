package me.gmx;

import me.gmx.process.ProcessTemplate;
import me.gmx.process.nodes.ComplementLabelNode;
import me.gmx.process.nodes.LabelNode;
import me.gmx.process.nodes.LabelNodeFactory;
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
            Set<LabelNode> actionable = template.getActionableLabels();
            //Find complements for tau
            HashMap<LabelNode, ComplementLabelNode> tauMatches
                    = SetUtil.getTauMatches(actionable);
            for (LabelNode na : template.getActionableLabels()){
                System.out.println(na.origin());
            }
            for (Map.Entry<LabelNode,ComplementLabelNode> e : tauMatches.entrySet()){
                System.out.println(String.format(
                        "Tau{%s, %s}"
                        , e.getKey().origin()
                        , e.getValue().origin()
                ));
            }
            System.out.println("------------");

            System.out.println(String.format("%s", template.prettyString()));
            System.out.println("Please type the label you'd like to act on:");
            String st = scan.next();
            //a.getActionableLabels()
            if (st == "") continue;
            LabelNode n = LabelNodeFactory.parseNode(st);

            try{
                System.out.println(String.format("%s -%s-> %s",
                        template.prettyString(),n.origin(),template.actOn(n).prettyString()));
            }catch (Exception e){
                System.out.println("Could not act on label!");
            }

        }
        return true;
    }


}
