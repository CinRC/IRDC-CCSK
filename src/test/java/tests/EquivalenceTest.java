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
    Process p1 = new CCSParser().parseLine("a|b").export();
    Process p2 = new CCSParser().parseLine("a.b+b.a").export();

    assert !p1.equals(p2);

    p1 = new CCSParser().parseLine("(a.b+b.a)|(b.a+a.b)").export();
    p2 = new CCSParser().parseLine("(a|b)|(a|b)").export();

    assert !p1.equals(p2);
  }

  @Test
  public void testEquivalence(){
    IRDC.config.clear();
    Process p1 = new CCSParser().parseLine("a.b.c|c.a.b").export();
    Process p2 = new CCSParser().parseLine("a.b.c|c.a.b").export();

    assert p1.equals(p2, false);

    p1 = new CCSParser().parseLine("a.b.c.d.P").export();
    p2 = new CCSParser().parseLine("a.b.c.d.Q").export();

    assert !p1.equals(p2);

    IRDC.config.add(RCCSFlag.PROCESS_NAMES_EQUIVALENT);
    p1 = new CCSParser().parseLine("a.b.c.d.P").export();
    p2 = new CCSParser().parseLine("a.b.c.d.Q").export();

    assert p1.equals(p2);
    IRDC.config.clear();
  }
}
