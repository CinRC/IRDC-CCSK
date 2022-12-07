import me.gmx.RCCS;
import me.gmx.parser.CCSParser;
import me.gmx.parser.LTTNode;
import me.gmx.process.ProcessContainer;
import me.gmx.process.nodes.Label;
import me.gmx.process.nodes.LabelFactory;
import me.gmx.process.nodes.LabelKey;
import me.gmx.process.nodes.TauLabelNode;
import me.gmx.process.process.Process;
import me.gmx.util.RCCSFlag;
import org.junit.jupiter.api.Test;

public class EnumerationTest {


    //Just make sure it doesnt crash
    @Test
    public void testEnumerate(){
        RCCS.config.clear();

        RCCS.config.add(RCCSFlag.DISPLAY_NULL);
        RCCS.config.add(RCCSFlag.DIFFERENTIATE_LABELS);
        RCCS.config.add(RCCSFlag.DEBUG);
        Process p = CCSParser.parseLine("a.b|c.d").export();
        Process pc = p.clone();
        ProcessContainer p1 = new ProcessContainer(p);
        ProcessContainer p2 = new ProcessContainer(pc);
        Label a, b;
        a = LabelFactory.createLabel("a",0);
        b = LabelFactory.createLabel("b",1);
        LabelKey la,lb;
        la = LabelFactory.createDebugLabelKey(a);
        lb = LabelFactory.createDebugLabelKey(b);
        System.out.println(p1.prettyString());
        p1.act(a);
        System.out.println(p1.prettyString());
        p1.reverseOn(la);
        System.out.println(p1.prettyString());
        p1.act(a);
        p1.act(b);
        System.out.println(p1.prettyString());
        p1.reverseOn(lb);
        p1.reverseOn(la);
        System.out.println(p1.prettyString());

    }

    //@Test
    //TODO: Broken
    public void test(){
        RCCS.config.clear();

        RCCS.config.add(RCCSFlag.DEBUG);
        RCCS.config.add(RCCSFlag.KEYS_MATCH_LABELS);
        ProcessContainer p = new ProcessContainer(CCSParser.parseLine("a.b|'a.c").export());
        Label a,aa,b,c;
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
    public void testTree(){
        RCCS.config.clear();
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
