import me.gmx.RCCS;
import me.gmx.parser.CCSParser;
import me.gmx.process.nodes.LabelFactory;
import me.gmx.process.process.Process;
import me.gmx.thread.ProcessContainer;
import me.gmx.util.RCCSFlag;
import org.junit.jupiter.api.Test;

public class EnumerationTest {

    @Test
    public void testEnumerate(){
        RCCS.config.add(RCCSFlag.DISPLAY_NULL);
        RCCS.config.add(RCCSFlag.DIFFERENTIATE_LABELS);
        Process p = CCSParser.parseLine("a.b|c.d").export();
        Process pc = p.clone();
        ProcessContainer p1 = new ProcessContainer(p);
        ProcessContainer p2 = new ProcessContainer(pc);

        System.out.println(p1.prettyString());
        p1.act(LabelFactory.createLabel("a", 0));
        System.out.println(p1.prettyString());
        p1.reverseLastAction();
        System.out.println(p1.prettyString());
        p1.act(LabelFactory.createLabel("a",0));
        p1.act(LabelFactory.createLabel("b",1));
        System.out.println(p1.prettyString());
        p1.reverseLastAction();
        p1.reverseLastAction();
        System.out.println(p1.prettyString());

    }
}
