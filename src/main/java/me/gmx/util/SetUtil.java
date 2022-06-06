package me.gmx.util;

import me.gmx.process.nodes.ComplementLabelNode;
import me.gmx.process.nodes.Label;
import me.gmx.process.nodes.LabelNode;
import me.gmx.process.nodes.TauLabelNode;
import me.gmx.process.process.Process;

import java.security.KeyPair;
import java.util.*;

public class SetUtil {


    public static String csvSet(Collection<Label> set){
        if (set.isEmpty())
            return "";
        StringBuilder sb = new StringBuilder();
        for (Label o : set){
            sb.append(o.origin());
            sb.append(",");
        }
        sb.deleteCharAt(sb.length()-1);
        return sb.toString();
    }

    /**
     * Returns a hashset of TauLabelNodes representing matching complements of labels provided in the given collection
     * @param nodes Collection of Labels to find tau matches from
     * @return Set of TauLabelNode that apply to the given collection
     */
    public static Collection<TauLabelNode> getTauMatches(Collection<Label> nodes){
        Set<TauLabelNode> tau = new HashSet<>();
        for (Label node : nodes){
            if (node instanceof ComplementLabelNode){
                //Cool, there is a complement in the set. Let's see if any matches
                for (Label innerNode : nodes)
                    if (innerNode != node)
                        if (node.isComplementOf(innerNode))
                            if (node.canSynchronize(innerNode) && innerNode.canSynchronize(node))
                            //Cool, we found a complement, let's add it to our map.
                            tau.add(new TauLabelNode(node,innerNode));
            }
        }
        return tau;

    }
}
