package tests;

import org.cinrc.IRDC;
import org.cinrc.parser.CCSParser;
import org.cinrc.parser.LTTNode;
import org.cinrc.process.process.Process;
import org.junit.jupiter.api.Test;

// Those tests are for forward-only simulations.

public class SimulationTest {

  @Test
  public void testSimulation1() {
    // p = a.b + a.c
    // q = a.(b + c)
    // q simulates p, but p cannot simulate q

    Process p, q;
    p = CCSParser.parseLine("a.b + a.c").export();
    q = CCSParser.parseLine("a.(b + c)").export();
    LTTNode node_p = new LTTNode(p);
    node_p.enumerate(true);
    LTTNode node_q = new LTTNode(q);
    node_q.enumerate(true);
    // The following is simply to help visualizing the process enumerations:
    /*
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
    */
    assert (node_q.canSimulate(node_p));
    assert (!node_p.canSimulate(node_q));
  }

  // This test makes sure that the channel used for a tau synchronization 
  // does not matter to simulation.
  @Test
  public void testSimulation2() {
    // p = (a | 'a)\a
    // q = (b | 'b)\b
    // q simulates p, and p can simulate q

    Process p, q;
    p = CCSParser.parseLine("(a | 'a)\\{a}").export();
    q = CCSParser.parseLine("(b | 'b)\\{b}").export();
    LTTNode node_p = new LTTNode(p);
    node_p.enumerate(true);
    LTTNode node_q = new LTTNode(q);
    node_q.enumerate(true);
    assert (node_q.canSimulate(node_p));
    assert (node_p.canSimulate(node_q));
  }


  // This test makes sure that prefix and parallel composition
  // behaves as expected wrt simulation.
  @Test
  public void testSimulation3() {
    // p = (a.c | 'a)\a
    // q = (b | 'b.c)\b
    // q simulates p, and p can simulate q

    Process p, q;
    p = CCSParser.parseLine("(a.c | 'a)\\{a}").export();
    q = CCSParser.parseLine("(b | 'b.c)\\{b}").export();
    LTTNode node_p = new LTTNode(p);
    node_p.enumerate(true);
    LTTNode node_q = new LTTNode(q);
    node_q.enumerate(true);
    assert (node_q.canSimulate(node_p));
    assert (node_p.canSimulate(node_q));
  }

  @Test
  public void testSimulation4() {
    // p = (a.b + b.a)
    // q = (a|b)
    // q simulates p, and p can simulate q

    Process p, q;
    p = CCSParser.parseLine("(a.b + b.a)").export();
    q = CCSParser.parseLine("(a | b)").export();
    LTTNode node_p = new LTTNode(p);
    node_p.enumerate(true);
    LTTNode node_q = new LTTNode(q);
    node_q.enumerate(true);
    assert (node_q.canSimulate(node_p));
    assert (node_p.canSimulate(node_q));
  }

  @Test
  public void testSimulation5() {
    // p = (a.b)
    // q = (a|b)
    // q simulates p, and p cannot simulate q

    Process p, q;
    p = CCSParser.parseLine("(a.b)").export();
    q = CCSParser.parseLine("(a | b)").export();
    LTTNode node_p = new LTTNode(p);
    node_p.enumerate(true);
    LTTNode node_q = new LTTNode(q);
    node_q.enumerate(true);
    assert (node_q.canSimulate(node_p));
    assert (!node_p.canSimulate(node_q));
  }

  @Test
  public void testSimulation6() {
    // p = (a|'a)\{a} + a
    // q = a |'a \{a}
    // q simulates p, and p can simulate q

    Process p, q;
    p = CCSParser.parseLine("(a.b)").export();
    q = CCSParser.parseLine("(a | b)").export();
    LTTNode node_p = new LTTNode(p);
    node_p.enumerate(true);
    LTTNode node_q = new LTTNode(q);
    node_q.enumerate(true);
    assert (node_q.canSimulate(node_p));
    assert (!node_p.canSimulate(node_q));
  }

  @Test
  public void testSimulation7() {
    // p = a.((b.c) + (b.d))
    // q = a.b.(c+d)
    // q simulates p, and p cannot simulate q

    Process p, q;
    p = CCSParser.parseLine("a.((b.c)+(b.d))").export();
    q = CCSParser.parseLine("a.b.(c+d)").export();
    LTTNode node_p = new LTTNode(p);
    node_p.enumerate(true);
    LTTNode node_q = new LTTNode(q);
    node_q.enumerate(true);
    assert (node_q.canSimulate(node_p));
    assert (!node_p.canSimulate(node_q));
  }

  @Test
  public void testSimulation8() {
    // p = P
    // q = Q
    // q and p should not be able to simulate each other (we should assume that different
    // letters correspond to different processes), and yet they can.
    // Cf. https://github.com/CinRC/IRDC-CCSK/issues/55

    Process p, q;
    p = CCSParser.parseLine("P").export();
    q = CCSParser.parseLine("Q").export();
    LTTNode node_p = new LTTNode(p);
    node_p.enumerate(true);
    LTTNode node_q = new LTTNode(q);
    node_q.enumerate(true);
    assert (!node_q.canSimulate(node_p));
    assert (!node_p.canSimulate(node_q));
  }

  /* Now we check basic properties of our simulation, namely that it contains the structural equivalence. */
  /* The idea here would be to take CCS structural equivalence, ≡:
  
    P | Q ≡ Q | P 
    (P | Q) | V ≡ P | (Q | V ) 
    P | 0 ≡ P
    P + Q ≡ Q + P 
    (P + Q) + V ≡ P + (Q + V)
    P + 0 ≡ P
    (P\a) | Q ≡ (P | Q)\a with a not a free variable in Q
    (P\a)\b ≡ (P\b)\a

    And to illustrate that P ≡ Q ⇒ P ~ Q
    
    We cannot reason over all P and Q in our java tests, but we can take reasonably complicated processes and check that those implications.
    */
  // mvn -Dtest="tests.SimulationTest#simulationIsStructural*" test
  // To run only the tests in this category.

  // For instance, for the first rule, P | Q ≡ Q | P, we could have:
  @Test
  public void simulationIsStructural() {
    IRDC.config.clear();
    Process p, q;
    p = CCSParser.parseLine("(a + b) | c").export();
    q = CCSParser.parseLine("c | (a + b)").export();
    // Of course, those processes are too simple, but I don't know if / how I can
    // - declare two processes, p and q,
    // - make p1 be p | q and p2 be q | p
    // simply.
    LTTNode node_p = new LTTNode(p);
    node_p.enumerate(true);
    System.out.println(node_p);
    LTTNode node_q = new LTTNode(q);
    node_q.enumerate(true);
    System.out.println(node_q);

    assert (node_q.canSimulate(node_p));
    assert (node_p.canSimulate(node_q));
  }


}
