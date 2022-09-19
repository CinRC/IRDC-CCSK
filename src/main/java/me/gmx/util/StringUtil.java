package me.gmx.util;

import me.gmx.process.nodes.Label;

import java.util.LinkedList;
import java.util.Stack;

public class StringUtil {
    //00AF, FFE3, 0305
    public static final String BAR = "\u0304";

    public static String addBar(String s){
        StringBuilder ns = new StringBuilder();
        for (char c : s.toCharArray()){
            ns.append(c).append(BAR);
        }
        return ns.toString();
    }

    public static String representPrefixes(LinkedList<Label> prefixes){
        StringBuilder sb = new StringBuilder();
        for (Label l : prefixes)
            sb.append(l).append(".");
        return sb.toString();



    }
}
