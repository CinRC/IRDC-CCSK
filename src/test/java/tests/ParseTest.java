package tests;

import org.cinrc.IRDC;
import org.cinrc.parser.CCSParser;
import org.cinrc.parser.CCSParserException;
import org.cinrc.process.ProcessContainer;
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
    Process p = new CCSParser().parseLine("a+b|c");
    ProcessContainer pc = new ProcessContainer(p);
    assert (pc.getProcess() instanceof SummationProcess);
    //Make sure a+b|c is parsed as (a+(b|c\abc))
    p = new CCSParser().parseLine("a+b|c\\{a,b,c}");
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
      Process p = new CCSParser().parseLine(s);
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
      Process p = new CCSParser().parseLine(s);
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
      Process p = new CCSParser().parseLine(given[i]);
      assert (
          compare(p.represent(), expected[i])
      );
    }
  }

  @Test
  public void testUnreachable() {
    IRDC.config.clear();
    boolean isFailed = false;

    try {
      new CCSParser().parseLine("Tau{a}[k0] | Tau{a}[k1]");
      isFailed = false;
    }catch (CCSParserException e){
      isFailed = true;
    }


    try {
      new CCSParser().parseLine("a.b.c[k0].P");
    }catch (CCSParserException e){
      isFailed = true;
    }

    try {
      new CCSParser().parseLine("Tau{a}[k0].P");
    }catch (CCSParserException e){
      isFailed = true;
    }

    try {
      new CCSParser().parseLine("Tau{a}[k0] | Tau{b}[k0]");
      isFailed = false;
    }catch (CCSParserException e){
      isFailed = true;
    }



    assert (isFailed == true);
    }

  @Test
  public void testInvalidParse() {
    IRDC.config.clear();
    boolean isFailed = false;

    try {
      new CCSParser().parseLine("ab");

    } catch (Exception e) {
      isFailed = true;
    }
    assert (isFailed);


    try {
      new CCSParser().parseLine("a+b+");
    } catch (Exception e) {
      isFailed = true;
    }
    assert (isFailed);

    try {
      new CCSParser().parseLine("a++");
    } catch (Exception e) {
      isFailed = true;
    }
    assert (isFailed);

  }

  @Test
  public void testNewEnum(){
    CCSParser parser = new CCSParser();

    parser.parseLine("a\\{a,b}");
    /*parser.parseLine("Tau{a}[k0]");
    parser.parseLine("a.b.P + (Tau{z}[k0]|Tau{z}[k0])");
    parser.parseLine("(a.b)|( (c.d)+(e.f) )");
    parser.parseLine("((a.b)|(c.d))");
    parser.parseLine("a|(b+c)");
    parser.parseLine("a.b.(c|d)");*/
  }

}
