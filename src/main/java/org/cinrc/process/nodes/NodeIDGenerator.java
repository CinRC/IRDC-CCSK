package org.cinrc.process.nodes;


//Just a thread safe n+1 generator for node ids
public class NodeIDGenerator {

  private static int c, k = 0;

  public static synchronized int nextAvailable() {
    return c++;
  }

  public static synchronized int value() {
    return c;
  }

  public static synchronized int valueKey() {
    return k;
  }

  public static synchronized int nextAvailableKey() {
    return k++;
  }

  public static synchronized void decrementKey() {
    k--;
  }

  public static void reset(){
    k = 0;
    c = 0;
  }

}
