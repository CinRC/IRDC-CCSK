import me.gmx.parser.CCSParser;

import java.util.Set;

public class OriginMatchTest {

    public void OriginMatch(){
        me.gmx.process.process.Process p = CCSParser.parseLine("a.b.P").export();
        assert p.origin().equals("a.b.P");


    }

}
