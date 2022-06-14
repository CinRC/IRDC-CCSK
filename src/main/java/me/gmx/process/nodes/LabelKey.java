package me.gmx.process.nodes;

import me.gmx.RCCS;

import java.util.UUID;

public class LabelKey extends Label {

    private UUID uuid;

    private Label from;
    public LabelKey(Label node){
        this.id = node.getId();
        this.from = node;
    }

    /**
     * To replicate UUIDs for tau transitions
     * @param node
     * @param id
     */
    public LabelKey(Label node, UUID id){
        this.id = id;
        this.from = node;
    }

    @Override
    public String toString(){
        return origin();
    }

    @Override
    public boolean equals(Object o){
        if (!(o instanceof LabelKey))
            return false;

        LabelKey key = (LabelKey) o;
        if (RCCS.KEY_MATCHING_MODE == 0)
            return key.getId().equals(getId()); //Match IDs
        else if (RCCS.KEY_MATCHING_MODE == 1)
            return key.origin().equals(origin());


        return key.getId().equals(getId()); //defaults to id
    }


    @Override
    public String origin() {
        return String.format("[%s]",from.origin());
    }

    public LabelKey clone(){
        return new LabelKey((Label)from.clone(), from.getId());
    }
}
