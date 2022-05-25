package me.gmx.util;

import me.gmx.process.CCSTransition;
import me.gmx.process.nodes.LabelKey;

import java.util.Hashtable;

/**
 * Something like a table maybe? Not really sure how this is going to work.
 * Maybe something like key -> {(process 1), (process 2)}
 */
public class ReversibilityHelper {

    private Hashtable<LabelKey, CCSTransition> reversibilityTable;

    public ReversibilityHelper(){
        reversibilityTable = new Hashtable<>();
    }

    //TODO: Implement? May not be needed
    public static Process addReversibilityStep(Process process, Process step){

        return null;
    }


}
