package me.gmx.parser;

import me.gmx.process.nodes.ComplementLabelNode;
import me.gmx.process.nodes.LabelNode;
import me.gmx.process.process.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum CCSGrammar {

    LABEL("[a-z]", LabelNode.class, null),
    WHITESPACE(" ", null, " "),
    OPEN_PARENTHESIS("(", null, "("),
    CLOSE_PARENTHESIS(")",null, ")"),
    PROCESS("[A-Z]", ProcessImpl.class, null),
    NULL_PROCESS("[A-Z0]",NullProcess.class,"0"),
    OP_SEQUENTIAL("\\.", null, "."),
    OUT_LABEL(String.format("'%s",LABEL.pString), ComplementLabelNode.class, null),
    OP_ACTIONPREFIX(String.format("(((%s)|(%s))%s)",
            LABEL.pString,OUT_LABEL.pString,OP_SEQUENTIAL.pString),null, null),
    OP_ACTIONPREFIX_REVERSE(String.format("%s(%s|%s)", OP_SEQUENTIAL.pString,LABEL.pString,OUT_LABEL.pString),null, null),
    ACTIONPREFIX_COMPLETE(String.format("(%s)*%s",OP_ACTIONPREFIX.pString,PROCESS.pString), ActionPrefixProcess.class, null),
    OP_CONCURRENT("\\|", ConcurrentProcess.class, "|"),
    OP_SUMMATION("\\+", SummationProcess.class, "+"),
    RESTRICTION(String.format("\\{(%s,?)*}",LABEL.pString), null, null);
    //RESTRICTED_PROCESS(String.format("(%s)%s",PROCESS.pString,RESTRICTION.pString), RestrictedProcessImpl.class, null);

    private String pString, rep;
    private Pattern pattern;
    private Class classObject;
    CCSGrammar(String s, Class c, String rep){
        this.pString = s;
        this.classObject = c;
        this.rep = rep;
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
        return rep == null ? "" : rep;
    }


}
