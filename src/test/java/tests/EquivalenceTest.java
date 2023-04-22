package tests;

import org.cinrc.IRDC;
import org.cinrc.parser.CCSParser;
import org.cinrc.util.RCCSFlag;
import org.junit.jupiter.api.Test;
import org.cinrc.process.process.Process;
import org.w3c.dom.css.CSSPageRule;

public class EquivalenceTest {

  @Test
  public void testNonEquivalence(){
    IRDC.config.clear();
    Process p1 = new CCSParser().parseLine("a|b");
    Process p2 = new CCSParser().parseLine("a.b+b.a");

    assert !p1.equals(p2);

    p1 = new CCSParser().parseLine("(a.b+b.a)|(b.a+a.b)");
    p2 = new CCSParser().parseLine("(a|b)|(a|b)");

    assert !p1.equals(p2);
  }

  @Test
  public void testEquivalence(){
    IRDC.config.clear();
    Process p1 = new CCSParser().parseLine("a.b.c|c.a.b");
    Process p2 = new CCSParser().parseLine("a.b.c|c.a.b");

    assert p1.equals(p2, false);

    p1 = new CCSParser().parseLine("a.b.c.d.P");
    p2 = new CCSParser().parseLine("a.b.c.d.Q");

    assert !p1.equals(p2);

    IRDC.config.add(RCCSFlag.PROCESS_NAMES_EQUIVALENT);
    p1 = new CCSParser().parseLine("a.b.c.d.P");
    p2 = new CCSParser().parseLine("a.b.c.d.Q");

    assert p1.equals(p2);
    IRDC.config.clear();
  }
}
