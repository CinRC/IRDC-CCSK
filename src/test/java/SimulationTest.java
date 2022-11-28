import java.util.Collections;
import me.gmx.parser.CCSParser;
import me.gmx.parser.LTTNode;
import me.gmx.process.nodes.Label;
import me.gmx.process.process.Process;
import org.junit.jupiter.api.Test;

public class SimulationTest {

  @Test
  public void testSimulation() {
    Process p, q;
    //q simulates p, but p cannot simulate q
    p = CCSParser.parseLine("a.b + a.c").export();
    q = CCSParser.parseLine("a.(b + c)").export();
    LTTNode node_p = new LTTNode(p);
    node_p.enumerate(true);
    LTTNode node_q = new LTTNode(q);
    node_q.enumerate(true);
    System.out.println(node_p.toString());
    for (Label l : Collections.unmodifiableSet(node_p.children.keySet())) {
      System.out.printf("%s, ", l);
    }
    System.out.println();
    System.out.println(node_q.toString());
    for (Label l : Collections.unmodifiableSet(node_q.children.keySet())) {
      System.out.printf("%s, ", l);
    }
    System.out.println("\n\n\n");

    assert (node_q.canSimulate(node_p));
    assert (!node_p.canSimulate(node_q));


  }
}
