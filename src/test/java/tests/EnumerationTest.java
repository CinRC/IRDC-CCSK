package tests;

import org.cinrc.IRDC;
import org.cinrc.parser.CCSParser;
import org.cinrc.parser.LTTNode;
import org.cinrc.process.ProcessContainer;
import org.cinrc.process.nodes.Label;
import org.cinrc.process.nodes.LabelFactory;
import org.cinrc.process.nodes.LabelKey;
import org.cinrc.process.nodes.TauLabelNode;
import org.cinrc.process.process.Process;
import org.cinrc.util.RCCSFlag;
import org.junit.jupiter.api.Test;

public class EnumerationTest {


  //Just make sure it doesnt crash
  @Test
  public void testEnumerate() {
    IRDC.config.clear();

    IRDC.config.add(RCCSFlag.DISPLAY_NULL);
    IRDC.config.add(RCCSFlag.DIFFERENTIATE_LABELS);
    IRDC.config.add(RCCSFlag.DEBUG);
    Process p = CCSParser.parseLine("a.b|c.d").export();
    Process pc = p.clone();
    ProcessContainer p1 = new ProcessContainer(p);
    ProcessContainer p2 = new ProcessContainer(pc);
    Label a, b;
    a = LabelFactory.createLabel("a", 0);
    b = LabelFactory.createLabel("b", 1);
    LabelKey la, lb;
    la = LabelFactory.createDebugLabelKey(a);
    lb = LabelFactory.createDebugLabelKey(b);
    System.out.println(p1.prettyString());
    p1.act(a);
    System.out.println(p1.prettyString());
    p1.act(la);
    System.out.println(p1.prettyString());
    p1.act(a);
    p1.act(b);
    System.out.println(p1.prettyString());
    p1.act(lb);
    p1.act(la);
    System.out.println(p1.prettyString());

  }

  //@Test
  //TODO: Broken
  public void test() {
    IRDC.config.clear();

    IRDC.config.add(RCCSFlag.DEBUG);
    IRDC.config.add(RCCSFlag.KEYS_MATCH_LABELS);
    ProcessContainer p = new ProcessContainer(CCSParser.parseLine("a.b|'a.c").export());
    Label a, aa, b, c;
    a = LabelFactory.createDebugLabel("a");
    aa = LabelFactory.createDebugLabel("'a");
    TauLabelNode tau = new TauLabelNode(a, aa);
    b = LabelFactory.createDebugLabel("b");
    c = LabelFactory.createDebugLabel("c");
    System.out.println(p.prettyString());
    p.act(tau);
    System.out.println(p.prettyString());
    p.act(b);
    System.out.println(p.prettyString());
    p.act(c);
    System.out.println(p.prettyString());
    System.out.println("Reversing process to initial state...");
    p.reverseLastAction();
    System.out.println(p.prettyString());
    p.reverseLastAction();
    System.out.println(p.prettyString());

  }

  @Test
  public void testTree() {
    IRDC.config.clear();
    Process p = CCSParser.parseLine("(a|b)|('a+'b)").export();
    System.out.printf("Testing enumeration of %s...\n", p.represent());
    LTTNode node = new LTTNode(p);
    node.enumerate(true);
    System.out.println(node);
    System.out.printf("Max depth: %d\n\n\n", node.getMaxDepth());
    assert (node.getMaxDepth() == 3);

    p = CCSParser.parseLine("a.(c|d)").export();
    System.out.printf("Testing enumeration of %s...\n", p.represent());
    node = new LTTNode(p);
    node.enumerate(true);
    System.out.println(node);
    System.out.printf("Max depth: %d\n\n\n", node.getMaxDepth());
    assert (node.getMaxDepth() == 3);

        /*p = CCSParser.parseLine("(d.(a+c)) | ('a.( (c|b)|'b) | (a+'b) )").export();
        System.out.printf("Testing enumeration of %s...\n", p.represent());
        node = new LTTNode(p);
        node.enumerate();
        System.out.println(node);
        System.out.printf("Max depth: %d\n\n\n", node.getMaxDepth());
        assert (node.getMaxDepth() == 8);*/


  }
}
