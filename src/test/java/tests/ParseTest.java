package tests;

import java.util.regex.Pattern;
import org.cinrc.IRDC;
import org.cinrc.parser.CCSGrammar;
import org.cinrc.parser.CCSParser;
import org.cinrc.parser.StringWalker;
import org.cinrc.parser.TestCCSGrammar;
import org.cinrc.process.ProcessContainer;
import org.cinrc.process.ProcessTemplate;
import org.cinrc.process.nodes.Label;
import org.cinrc.process.nodes.LabelFactory;
import org.cinrc.process.process.Process;
import org.cinrc.process.process.SummationProcess;
import org.cinrc.util.RCCSFlag;
import org.junit.jupiter.api.Test;

public class ParseTest {
  private boolean compare(String given, String expected) {
    System.out.println("[Test] Comparing " + given + " with expected result " + expected);
    return given.equals(expected);
  }

  @Test
  public void testPrecedence() {
    IRDC.config.clear();
    Label a = LabelFactory.createDebugLabel("a");
    Label b = LabelFactory.createDebugLabel("b");
    Label c = LabelFactory.createDebugLabel("c");
    //Make sure a + b | c is parsed as (a+(b|c))
    Process p = new CCSParser().parseLine("a+b|c").export();
    ProcessContainer pc = new ProcessContainer(p);
    assert (pc.getProcess() instanceof SummationProcess);
    //Make sure a+b|c is parsed as (a+(b|c\abc))
    p = new CCSParser().parseLine("a+b|c\\{a,b,c}").export();
    pc = new ProcessContainer(p);
    assert (!pc.canAct(c));
    assert (pc.canAct(a));
    assert (pc.canAct(b));
  }


  @Test
  public void testOriginMatching() {
    IRDC.config.clear();
    String[] matchTest;
    matchTest = new String[] {
        "a.b.P",
        "(a|b)",
        "(P|Q)",
        "(0|0)",
        "(a.b.P|a.b.P)",
        "(a.P|(a+b))"
    };
    for (String s : matchTest) {
      Process p = new CCSParser().parseLine(s).export();
      String a = p.represent();
      assert compare(a, s);
    }

    IRDC.config.add(RCCSFlag.HIDE_PARENTHESIS);

    matchTest = new String[] {
        "a.b.P",
        "a|b",
        "P|Q",
        "0|0",
        "a.b.P|a.b.P",
        "a.P|a+b"
    };

    for (String s : matchTest) {
      Process p = new CCSParser().parseLine(s).export();
      String a = p.represent();
      assert compare(a, s);
    }
  }

  @Test
  public void testRedundantParenthesis() {
    IRDC.config.clear();

    //There's likely a better way to do this with a hashmap, but it works.
    //Mainly just want to see if the program doesnt die when trying to parse. Result should be stable
    String[] given = new String[] {
        "(((((a)))))",
        "(a.b.'c.P) | ((( 'c.b.'a.P + ('a.b.Q) )))"
    };
    String[] expected = new String[] {
        "a",
        "(a.b.'c.P|('c.b.'a.P+'a.b.Q))"
    };
    for (int i = 0; i < expected.length; i++) {
      Process p = new CCSParser().parseLine(given[i]).export();
      assert (
          compare(p.represent(), expected[i])
      );

    }
  }

  @Test
  public void testBasicParse() {
  }

  @Test
  public void testInvalidParse() {
    IRDC.config.clear();
    boolean isFailed = false;

    ProcessTemplate t = new CCSParser().parseLine("ab");
    try {
      t.export();
    } catch (Exception e) {
      isFailed = true;
    }
    assert (isFailed);


    t = new CCSParser().parseLine("a+b+");
    try {
      t.export();
    } catch (Exception e) {
      isFailed = true;
    }
    assert (isFailed);

    t = new CCSParser().parseLine("a++");
    try {
      t.export();
    } catch (Exception e) {
      isFailed = true;
    }
    assert (isFailed);

  }

  @Test
  public void testWalker(){
    StringWalker walker = new StringWalker("helloTau{a}aa");
    do{
      walker.walk();
      if (walker.read() == 'T'){
        System.out.println(walker.peekUntil(Pattern.compile(CCSGrammar.TAU_LABEL.pString)));
      }
      walker.clearMemory();
    }while(walker.canWalk());
  }

  @Test
  public void testNewEnum(){
    CCSParser parser = new CCSParser();

    /*Process p = parser.debugParse("a.b.c.d.P");
    System.out.println(p);*/

//    parser.debugParse("a.b.P + (Tau{z}[k0]|Tau{z}[k0])");
//    parser.debugParse("(a.b)|( (c.d)+(e.f) )");
    //parser.debugParse("((a.b)|(c.d))");
    parser.debugParse("a|(b+c)");
  }

}
