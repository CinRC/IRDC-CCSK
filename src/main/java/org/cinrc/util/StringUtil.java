package org.cinrc.util;

import java.util.LinkedList;
import org.cinrc.process.nodes.Label;

public class StringUtil {
  //00AF, FFE3, 0305
  public static final String BAR = "\u0304";

  public static String addBar(String s) {
    StringBuilder ns = new StringBuilder();
    for (char c : s.toCharArray()) {
      ns.append(c).append(BAR);
    }
    return ns.toString();
  }

  public static String representPrefixes(LinkedList<Label> prefixes) {
    StringBuilder sb = new StringBuilder();
    for (Label l : prefixes) {
      sb.append(l).append(".");
    }
    return sb.toString();
  }


}
