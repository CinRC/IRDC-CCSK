package me.gmx.util;

import me.gmx.RCCS;
import me.gmx.process.nodes.*;
import me.gmx.process.process.ComplexProcess;
import me.gmx.process.process.ConcurrentProcess;
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
                    if (innerNode != node && !(innerNode instanceof LabelKey))
                        if (node.isComplementOf(innerNode))
                            if (node.canSynchronize(innerNode) && innerNode.canSynchronize(node)) {
                                //Cool, we found a complement, let's add it to our map.
                                TauLabelNode n = new TauLabelNode(node, innerNode); //Don't want duplicates
                                if (!tau.contains(n))
                                    tau.add(n);
                            }
            }
        }
        return tau;
    }

    public static Collection<Label> removeUnsyncableKeys(Process p, Collection<Label> labels){
        Collection<Label> synced = new HashSet<>();

        for (Label l : labels){
            //we only care about keys and unsynced keys
            if (!(l instanceof LabelKey))
                continue;

            LabelKey key1 = ((LabelKey)l);
            if (key1.from instanceof TauLabelNode){
                if (!recursiveIsSyncable(p,key1))
                    synced.add(key1);
            }
        }
        labels.removeAll(synced);
        return labels;
    }

    public static boolean recursiveIsSyncable(Process p, LabelKey key){
        if (!(p instanceof ComplexProcess))
            return false;

        System.out.println(String.format("Checking if %s is syncable to %s",
                p.represent(), key.origin()));
        ComplexProcess pr = (ComplexProcess) p;
        if (recursiveIsSyncable(pr.left, key) && recursiveIsSyncable(pr.right,key)) {
            System.out.println(String.format("%s is syncable to %s!",
                    p.represent(), key.origin()));
            return true;
        }
        else return false;
    }
}