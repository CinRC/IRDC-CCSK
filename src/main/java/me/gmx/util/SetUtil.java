package me.gmx.util;

import me.gmx.process.nodes.LabelNode;

import java.util.Collection;

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
}
