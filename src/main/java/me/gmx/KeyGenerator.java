package me.gmx;

import me.gmx.process.nodes.LabelKey;

import java.util.Random;

public class KeyGenerator {

    private int generation;

    /**
     * - Keys must be unique
     * - Keyspace must be infinite
     * - (optional) Must be able to converge at a common generator
     */

    //Temporarily, we'll just use random generation
    Random random;

    private long gen;
    public KeyGenerator(){
        gen = 0;
        random = new Random();
    }

    public long generateKey(){
        return random.nextLong();
        //return (gen++)*2+1;
    }

    /**
     * 1, 3, 5, 7, 9, 11
     *
     * 3, 7
     */


}
