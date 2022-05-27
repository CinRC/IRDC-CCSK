package me.gmx.process.nodes;

import java.util.UUID;

public class LabelKey extends LabelNode {

    private UUID uuid;

    public LabelKey(LabelNode node){
        super(node.origin());
        this.uuid = UUID.randomUUID();
    }

    /**
     * To replicate UUIDs for tau transitions
     * @param node
     * @param id
     */
    public LabelKey(LabelNode node, UUID id){
        super(node.origin());
        this.uuid = id;
    }

    @Override
    public String toString(){
        return this.uuid.toString();
    }


    @Override
    public String origin() {
        return null;
    }
}
