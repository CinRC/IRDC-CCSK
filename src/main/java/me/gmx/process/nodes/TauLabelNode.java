package me.gmx.process.nodes;

//Making a separate class because I want to store the two labels that are synchronizing.
//TODO: NYI
public class TauLabelNode extends Label {

    private Label node, complement;
    public boolean consumeLeft, consumeRight;

    public TauLabelNode(Label node, Label comp) {
        this.node = node;
        this.complement = comp;
        consumeLeft = consumeRight = false;
    }


    @Override
    public boolean equals(Object o){
        if (!(o instanceof TauLabelNode))
            return false;
        TauLabelNode node = (TauLabelNode) o;
        return node.getA().equals(getA()) && node.getB().equals(getB());
    }

    @Override
    public String origin(){
        return String.format("Tau{%s, %s}",node.origin(),complement.origin());
    }

    public Label getA(){
        return node;
    }
    public Label getB(){
        return complement;
    }

    @Override
    public TauLabelNode clone(){
        return new TauLabelNode((Label) getA().clone(),(Label) getB().clone());
    }

}
