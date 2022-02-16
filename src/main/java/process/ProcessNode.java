package process;

import java.util.regex.Pattern;

public class ProcessNode {
    private String origin;
    private Pattern pattern;
    public ProcessNode(Pattern p, CharSequence c){
        origin = c.toString();
        pattern = p;
    }

    public String toString(){
        return String.format("[%s]");
    }

}
