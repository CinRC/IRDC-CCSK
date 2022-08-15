package me.gmx.process.nodes;

import me.gmx.RCCS;
import me.gmx.util.RCCSFlag;

import java.util.UUID;

public class LabelKey extends Label {

    public Label from;
    public LabelKey(Label node){
        super(node.dupe, node.getChannel());
        this.id = UUID.randomUUID();
        this.from = node;
        if (node instanceof TauLabelNode)
            this.dupe = ((TauLabelNode)node).saveDupe;
        else
            this.dupe = NodeIDGenerator.nextAvailableKey();
    }

    /**
     * To replicate UUIDs for tau transitions
     * @param key LabelKEy to copy from
     */
    public LabelKey(LabelKey key){
        super(key.dupe, key.getChannel());
        id = key.getId();
        from = key.from.clone();
        origin = key.origin();
        this.dupe = key.dupe;
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
        //return key.getId().equals(getId()); //defaults to id
        //return (key.getChannel().equals(getChannel()) && (key.dupe == dupe ? true : key.dupe == -1));
        return key.from.equals(from) && (key.dupe == dupe || key.dupe == -1 || dupe == -1);
    }


    @Override
    public String origin() {
        if (RCCS.config.contains(RCCSFlag.KEYS_MATCH_LABELS))
            return String.format("[%s]",from.origin());
        else return String.format("[k%s]", dupe);
    }

    public LabelKey clone(){
        return new LabelKey(this);
    }
}
