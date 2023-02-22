package tests;

import java.util.Collection;
import org.cinrc.IRDC;
import org.cinrc.parser.CCSParser;
import org.cinrc.process.ProcessContainer;
import org.cinrc.process.nodes.Label;
import org.cinrc.process.nodes.LabelFactory;
import org.cinrc.process.nodes.LabelKey;
import org.cinrc.process.nodes.TauLabelNode;
import org.cinrc.process.process.Process;
import org.junit.jupiter.api.Test;

public class ActionTest {


  @Test
  public void temporaryTest() {
    Process p = CCSParser.parseLine("(a+b)|('a|'b)").export();
    ProcessContainer pc = new ProcessContainer(p);
    TauLabelNode n = new TauLabelNode(LabelFactory.createDebugLabel("a"),
        LabelFactory.createDebugLabel("'a"));
    LabelKey nkey = new LabelKey(n);
    Label l = LabelFactory.createDebugLabel("b");
    pc.act(l);
    pc.act(n);
    pc.act(nkey);
    pc.act(n);
    pc.act(nkey);
    pc.act(n);
  }

  @Test
  public void testBasicActions() {
    IRDC.config.clear();

    Label a, b, c;
    a = LabelFactory.createDebugLabel("a");
    b = LabelFactory.createDebugLabel("b");
    c = LabelFactory.createDebugLabel("c");
    ProcessContainer p = new ProcessContainer(CCSParser.parseLine("a.b").export());
    p.act(a);
    assert p.canAct(b);
  }

  @Test
  public void testAdvancedActions() {
    IRDC.config.clear();

    Label a, b, c, d;
    a = LabelFactory.createDebugLabel("a");
    b = LabelFactory.createDebugLabel("b");
    c = LabelFactory.createDebugLabel("c");
    d = LabelFactory.createDebugLabel("d");
    LabelKey la, lb, lc, ld;
    la = LabelFactory.createDebugLabelKey(a);
    lb = LabelFactory.createDebugLabelKey(b);
    lc = LabelFactory.createDebugLabelKey(c);
    ld = LabelFactory.createDebugLabelKey(d);
    ProcessContainer p = new ProcessContainer(CCSParser.parseLine("a.b+c.d").export());
    assert p.canAct(a) && p.canAct(c);
    assert !p.canAct(b) && !p.canAct(d);
    p.act(a); // a.b+c.d -a-> [k0]b
    assert p.canAct(b) && !p.canAct(c) && !p.canAct(a);
    p.act(la); //[k0]b -[k0]-> a.b+c.d
    assert p.canAct(a) && p.canAct(c);
    assert !p.canAct(b) && !p.canAct(d);

    p.act(a);
    p.act(b);
    Collection<Label> act = p.getActionableLabels();
    act.remove(p.getKey());
    assert act.isEmpty();
    p.act(lb);
    p.act(la);

    assert p.canAct(a) && p.canAct(c);
    assert !p.canAct(b) && !p.canAct(d);
  }

}
