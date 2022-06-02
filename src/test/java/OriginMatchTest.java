import me.gmx.parser.CCSParser;

import java.util.Set;

public class OriginMatchTest {

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

}
