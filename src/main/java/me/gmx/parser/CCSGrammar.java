package me.gmx.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum CCSGrammar {

    OP_CONCURRENT("\\|"),               // |
    OP_SUMMATION("\\+"),                // +
    OP_SEQUENTIAL("\\."),               // .
    WHITESPACE(" "),                    //
    PROCESS("[A-Z]"),                   // P
    LABEL("[a-z]"),                     // a
    OUT_LABEL("'"+LABEL.pString),       // 'a
    RESTRICTION("\\"+LABEL.pString);    // \a

    private String pString;
    private Pattern pattern;
    CCSGrammar(String s){
        this.pString = s;
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
        return pattern.toString();
    }


}
