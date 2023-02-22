package tests;

import org.cinrc.IRDC;
import org.cinrc.parser.CCSParser;
import org.cinrc.process.ProcessContainer;
import org.cinrc.process.nodes.Label;
import org.cinrc.process.nodes.LabelFactory;
import org.cinrc.process.nodes.TauLabelNode;
import org.cinrc.process.process.Process;
import org.junit.jupiter.api.Test;

public class RestrictionTest {

  /*
   * This test make sure that a process a\a cannot
   * act on a nor 'a.
   */
  @Test
  public void testRestrictionBlocking() {
    IRDC.config.clear();
    Label a, oa;
    a = LabelFactory.createDebugLabel("a");
    oa = LabelFactory.createDebugLabel("'a");
    Process p = CCSParser.parseLine("a\\{a}").export();

    ProcessContainer pc = new ProcessContainer(p);

    assert (!pc.canAct(a));
    assert (!pc.canAct(oa));
  }

  /*
   * This test make sure that a process b\a cannot
   * act on a nor 'a, but can act on b.
   */
  @Test
  public void testRestrictionNonBlocking() {
    IRDC.config.clear();
    Label a, oa, b;
    a = LabelFactory.createDebugLabel("a");
    oa = LabelFactory.createDebugLabel("'a");
    b = LabelFactory.createDebugLabel("b");
    Process p = CCSParser.parseLine("b\\{a}").export();

    ProcessContainer pc = new ProcessContainer(p);

    assert (!pc.canAct(a));
    assert (!pc.canAct(oa));
    assert (pc.canAct(b));
  }

  /*
   * This test make sure that a process (a|'a)\a can
   * act on Tau{a, 'a} and Tau{'a, a} (that is, the order
   * of labels in the tau label is not significant), but
   * that it cannot act on a nor 'a.
   */
  @Test
  public void testRestrictionSync() {
    IRDC.config.clear();
    Label a, oa, ta, taswitch;
    a = LabelFactory.createDebugLabel("a");
    oa = LabelFactory.createDebugLabel("'a");
    ta = new TauLabelNode(a, oa);
    taswitch = new TauLabelNode(oa, a);
    Process p = CCSParser.parseLine("(a|'a)\\{a}").export();

    ProcessContainer pc = new ProcessContainer(p);

    //System.out.printf("Asserting that %s cannot act on %s...\n", pc.prettyString(), a);
    assert (!pc.canAct(a)); // (a|'a)\{a} cannot do a
    //System.out.printf("Asserting that %s cannot act on %s...\n", pc.prettyString(), oa);
    assert (!pc.canAct(oa)); // (a|'a)\{a} cannot do 'a
    //System.out.printf("Asserting that %s can act on %s...\n", pc.prettyString(), ta);
    assert (pc.canAct(ta)); // (a|'a)\{a} can do Tau(a, 'a)
    //System.out.printf("Asserting that %s can act on %s...\n", pc.prettyString(), taswitch);
    assert (pc.canAct(taswitch)); // (a|'a)\{a} can do Tau('a, a)
  }


}
