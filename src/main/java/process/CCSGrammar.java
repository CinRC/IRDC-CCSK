package process;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum CCSGrammar {

    OP_CONCURRENT("\\|"),
    OP_DETERMINISTIC("\\+"),
    OP_SEQUENTIAL("\\."),
    PROCESS("[PQV]"),
    LABEL("[abc]");

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

    String refactor(CharSequence c){
        return null;
    }


}
