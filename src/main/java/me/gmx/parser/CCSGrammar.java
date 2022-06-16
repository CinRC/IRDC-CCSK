package me.gmx.parser;

import me.gmx.process.nodes.ComplementLabelNode;
import me.gmx.process.nodes.Label;
import me.gmx.process.nodes.LabelNode;
import me.gmx.process.process.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum CCSGrammar {

    LABEL("[a-z]", LabelNode.class, null, false),
    WHITESPACE(" ", null, " ", false),
    OPEN_PARENTHESIS("\\(", null, "(", true),
    CLOSE_PARENTHESIS("\\)",null, ")", true),
    PROCESS("[A-Z]", ProcessImpl.class, null, true),
    NULL_PROCESS("[0]",NullProcess.class,"0", true),
    OP_SEQUENTIAL("\\.", null, ".", true),
    COMPLEMENT_SIG("'",null, "'", false),
    OUT_LABEL(String.format("'%s",LABEL.pString), ComplementLabelNode.class, null, false),
    OP_ACTIONPREFIX(String.format("(%s)|(%s)%s",
            LABEL.pString,OUT_LABEL.pString,OP_SEQUENTIAL.pString),null, null, false),
    LABEL_COMBINED(String.format("(%s)|(%s)",
            LABEL.pString,OUT_LABEL.pString), Label.class, null, true),
    OP_ACTIONPREFIX_REVERSE(String.format("%s(%s|%s)", OP_SEQUENTIAL.pString,LABEL.pString,OUT_LABEL.pString),null, null, false),
    ACTIONPREFIX_COMPLETE(String.format("(%s)*%s",OP_ACTIONPREFIX.pString,PROCESS.pString), ActionPrefixProcess.class, null, false),
    OP_CONCURRENT("\\|", ConcurrentProcess.class, "|", true),
    OP_SUMMATION("\\+", SummationProcess.class, "+", true),
    OPEN_RESTRICTION("\\\\\\{", null, null, true), //6 backslashes, LOL. \\{
    CLOSE_RESTRICTION("\\}", null, null, true);
//    RESTRICTION(String.format("\\{(%s,?)*}",LABEL.pString), null, null),
//    RESTRICTED_PROCESS(String.format("(%s)%s",PROCESS.pString,RESTRICTION.pString), RestrictedProcessImpl.class, null);

    private String pString, rep;
    private Pattern pattern;
    private Class classObject;
    private boolean canParse;

    /**
     *
     * @param s Regex to match against
     * @param c Instantiatable class representation
     * @param rep Human readable constant
     * @param canParse Should this be parseable
     */
    CCSGrammar(String s, Class c, String rep, boolean canParse){
        this.pString = s;
        this.classObject = c;
        this.rep = rep;
        this.canParse = canParse;
    }

    public Class getClassObject(){
        return classObject;
    }

    public boolean canBeParsed(){ return this.canParse; }

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
