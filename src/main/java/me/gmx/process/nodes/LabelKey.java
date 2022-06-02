package me.gmx.process.nodes;

import java.util.UUID;

public class LabelKey extends Label {

    private UUID uuid;

    private Label from;
    public LabelKey(Label node){
        this.uuid = UUID.randomUUID();
        this.from = node;
    }

    /**
     * To replicate UUIDs for tau transitions
     * @param node
     * @param id
     */
    public LabelKey(Label node, UUID id){
        this.uuid = id;
    }

    @Override
    public String toString(){
        return this.uuid.toString();
    }


    @Override
    public String origin() {
        return String.format("[%s]",from.origin());
    }

    public LabelKey clone(){
        return new LabelKey((Label)from.clone(), from.getId());
    }
}
