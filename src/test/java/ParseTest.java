import me.gmx.RCCS;
import me.gmx.parser.CCSParser;
import me.gmx.parser.CCSParserException;
import me.gmx.parser.CCSTransitionException;
import me.gmx.process.nodes.Label;
import me.gmx.process.nodes.LabelFactory;
import me.gmx.process.process.Process;
import me.gmx.process.process.SummationProcess;
import me.gmx.thread.ProcessContainer;
import me.gmx.thread.ProcessTemplate;
import me.gmx.util.RCCSFlag;
import org.junit.jupiter.api.Test;

public class ParseTest {
    private boolean compare(String given, String expected) {
        System.out.println("[Test] Comparing " + given + " with expected result " + expected);
        return given.equals(expected);
    }

    @Test
    public void testPrecedence(){
        RCCS.config.clear();
        Label a = LabelFactory.createDebugLabel("a");
        Label b = LabelFactory.createDebugLabel("b");
        Label c = LabelFactory.createDebugLabel("c");
        //Make sure a + b | c is parsed as (a+(b|c))
        Process p = CCSParser.parseLine("a+b|c").export();
        ProcessContainer pc = new ProcessContainer(p);
        assert(pc.getProcess() instanceof SummationProcess);

        //Make sure a+b|c is parsed as (a+(b|c\abc))
        p = CCSParser.parseLine("a+b|c\\{a,b,c}").export();
        pc = new ProcessContainer(p);
        assert(!pc.canAct(c));
        assert(pc.canAct(a));
        assert(pc.canAct(b));

    }


    @Test
    public void testOriginMatching() {
        RCCS.config.clear();
        String[] matchTest;
        matchTest = new String[]{
                "a.b.P",
                "(a|b)",
                "(P|Q)",
                "(0|0)",
                "(a.b.P|a.b.P)",
                "(a.P|(a+b))"
        };
        for (String s : matchTest) {
            String a = CCSParser.parseLine(s).export().origin();
            assert compare(a, s);
        }

        RCCS.config.add(RCCSFlag.HIDE_PARENTHESIS);

        matchTest = new String[]{
                "a.b.P",
                "a|b",
                "P|Q",
                "0|0",
                "a.b.P|a.b.P",
                "a.P|a+b"
        };

        for (String s : matchTest) {
            String a = CCSParser.parseLine(s).export().origin();
            assert compare(a, s);
        }
    }

    @Test
    public void testRedundantParenthesis() {
        RCCS.config.clear();

        //There's likely a better way to do this with a hashmap, but it works.
        //Mainly just want to see if the program doesnt die when trying to parse. Result should be stable
        String[] given = new String[]{
                "(((((a)))))",
                "(a.b.'c.P) | ((( 'c.b.'a.P + ('a.b.Q) )))"
        };
        String[] expected = new String[]{
                "a",
                "(a.b.'c.P|('c.b.'a.P+'a.b.Q))"
        };
        for (int i = 0; i < expected.length; i++)
            assert (
                    compare(CCSParser.parseLine(given[i]).export().origin(), expected[i])
            );
    }

    @Test
    public void testBasicParse() {
    }

    @Test
    public void testInvalidParse() {
        RCCS.config.clear();
        boolean isFailed = false;

        ProcessTemplate t = CCSParser.parseLine("ab");
        try {
            t.export();
        } catch (Exception e) {
            isFailed = true;
        }
        assert (isFailed);


        t = CCSParser.parseLine("a+b+");
        try {
            t.export();
        } catch (Exception e) {
            isFailed = true;
        }
        assert (isFailed);

        t = CCSParser.parseLine("a++");
        try {
            t.export();
        } catch (Exception e) {
            isFailed = true;
        }
        assert (isFailed);

    }

}
