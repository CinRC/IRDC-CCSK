import me.gmx.parser.CCSParser;

import java.util.Set;

public class ParseTest {

    //TODO: add maven/junit
    public void OriginMatch(){
        String[] matchTest = new String[]{
                "a.b.P",
                "a|b",
                "(a.b.P|a.b.P)",
                "a.P|(a+b)"
        };
        //Basically, can a process be parsed and tokenized while retaining its properties
        for (String s : matchTest)
            assert CCSParser.parseLine(s).export().origin().equals(s);
    }

    public void testParenthesis(){
        String[] str = new String[]{
                "(a.b.'c.P) | ((('c.b.'a.P + ('a.b.Q) )))"
        };

        for (String s : str)
            assert (
                    CCSParser.parseLine(s).export() != null
                    );

    }

}
