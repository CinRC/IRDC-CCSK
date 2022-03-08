package me.gmx.parser;

import me.gmx.process.nodes.ComplementLabelNode;
import me.gmx.process.nodes.LabelNode;
import me.gmx.process.process.*;

import javax.swing.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum CCSGrammar {

    LABEL("[a-z]", LabelNode.class),
    WHITESPACE(" ", null),
    PROCESS("[A-Z]", UnrestrictedProcess.class),
    OP_SEQUENTIAL("\\.", null),
    OUT_LABEL(String.format("'%s",LABEL.pString), ComplementLabelNode.class),
    OP_ACTIONPREFIX(String.format("(((%s)|(%s))%s)",
            LABEL.pString,OUT_LABEL.pString,OP_SEQUENTIAL.pString),null),
    OP_ACTIONPREFIX_REVERSE(String.format("%s(%s|%s)", OP_SEQUENTIAL.pString,LABEL.pString,OUT_LABEL.pString),null),
    ACTIONPREFIX_COMPLETE(String.format("(%s)*%s",OP_ACTIONPREFIX.pString,PROCESS.pString), ActionPrefixProcess.class),
    OP_CONCURRENT("\\|", ConcurrentProcess.class),
    OP_SUMMATION("\\+", SummationProcess.class),
    RESTRICTION(String.format("\\{(%s,?)*}",LABEL.pString), null),
    RESTRICTED_PROCESS(String.format("(%s)%s",PROCESS.pString,RESTRICTION.pString),RestrictedProcess.class);

    private String pString;
    private Pattern pattern;
    private Class classObject;
    CCSGrammar(String s, Class c){
        this.pString = s;
        this.classObject = c;
    }
    public Class getClassObject(){
        return classObject;
    }

    /***
     * Returns pattern object from stored string, caching it if first access.
     * @return Pattern object
     */
    Pattern getPattern(){
        return this.pattern == null ? Pattern.compile(this.pString) : this.pattern;
    }

    public Matcher match(CharSequence c){
        return getPattern().matcher(c);
    }

    CCSGrammar find(String s) throws Exception {
        for (CCSGrammar g : values()){
            if (g.match(s).find()){
                return g;
            }
        }
        throw new Exception(String.format("Cannot find grammar for: %s",s));
    }


    public String toString(){
        return pString;
    }


}
