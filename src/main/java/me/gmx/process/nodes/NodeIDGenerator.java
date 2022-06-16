package me.gmx.process.nodes;


//Just a thread safe n+1 generator for node ids
public class NodeIDGenerator {

    private static int c = 0;

    public static synchronized int nextAvailable() {
        return c++;
    }

    public static synchronized int value() {
        return c;
    }

}
