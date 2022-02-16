package process;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

public class ProcessFormula {

    int size;
    LinkedList<ProcessNode> nodes;
    FormulaType type;
    String rawFormula;

    public ProcessFormula(ProcessNode... nodes){
        this.nodes.addAll(List.of(nodes));
        this.size = nodes.length;
        this.type = FormulaType.CCS;
    }

    public void render(Pattern p){
        //TODO
    }

    enum FormulaType{
        CCS;
    }

}
