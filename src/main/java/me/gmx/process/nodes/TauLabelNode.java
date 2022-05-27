package me.gmx.process.nodes;

//Making a separate class because I want to store the two labels that are synchronizing.
//TODO: NYI
public class TauLabelNode extends Label {

    private Label node, complement;

    public TauLabelNode(Label node, Label comp) {
        this.node = node;
        this.complement = comp;
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

}
