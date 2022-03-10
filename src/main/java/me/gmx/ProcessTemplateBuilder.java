package me.gmx;

import me.gmx.parser.CCSGrammar;
import me.gmx.process.ProcessTemplate;
import me.gmx.process.process.ComplexProcess;

public class ProcessTemplateBuilder {


    public static ProcessTemplate combineTemplates(ProcessTemplate t1, ProcessTemplate t2, CCSGrammar operator){
        // If the ccsgrammar refers to a complex process
        if (ComplexProcess.class.isAssignableFrom(operator.getClassObject())){

        }
        return null;
    }
}
