package me.gmx.util;

import me.gmx.process.nodes.ComplementLabelNode;
import me.gmx.process.nodes.LabelNode;
import me.gmx.process.process.Process;

import java.security.KeyPair;
import java.util.*;

public class SetUtil {

    public static String csvSet(Collection<LabelNode> set){
        if (set.isEmpty())
            return "";
        StringBuilder sb = new StringBuilder();
        for (Object o : set){
            sb.append(o.toString());
            sb.append(",");
        }
        sb.deleteCharAt(sb.length()-1);
        return sb.toString();
    }

    public static HashMap<LabelNode, ComplementLabelNode> getTauMatches(Collection<LabelNode> nodes){
        HashMap<LabelNode, ComplementLabelNode> matches = new HashMap<>();
        for (LabelNode node : nodes){
            if (node instanceof ComplementLabelNode){
                //Cool, there is a complement in the set. Let's see if any matches
                for (LabelNode innerNode : nodes)
                    if (innerNode.isComplement((ComplementLabelNode) node))
                        //Cool, we found a complement, let's add it to our map.
                        matches.put(innerNode,(ComplementLabelNode) node);

            }
        }

        return matches;

    }
}
