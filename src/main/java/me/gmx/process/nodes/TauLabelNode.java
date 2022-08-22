package me.gmx.process.nodes;

import java.util.UUID;

//Making a separate class because I want to store the two labels that are synchronizing.
//TODO: NYI
public class TauLabelNode extends Label {

    private Label a, b;
    public boolean consumeLeft, consumeRight;
    int saveDupe;

    public TauLabelNode(Label node, Label comp) {
        super(-2, node.getChannel() + ":" + comp.getChannel());
        this.a = node;
        this.b = comp;
        consumeLeft = consumeRight = false;
        this.saveDupe = NodeIDGenerator.nextAvailableKey();
        this.id = UUID.randomUUID();
    }

    @Override
    public boolean equals(Object o){
        if (!(o instanceof TauLabelNode))
            return false;
        TauLabelNode node = (TauLabelNode) o;
        return (node.getA().equals(getA()) && node.getB().equals(getB()))
                || (node.getB().equals(getA()) && node.getA().equals(getB()));
    }

    @Override
    public String toString(){
        return String.format("Tau{%s, %s}",a,b);
    }

    public Label getA(){
        return a;
    }
    public Label getB(){
        return b;
    }

    @Override
    public TauLabelNode clone(){
        return new TauLabelNode((Label) getA().clone(),(Label) getB().clone());
    }

    public void destroy(){
        NodeIDGenerator.decrementKey();
    }

}
