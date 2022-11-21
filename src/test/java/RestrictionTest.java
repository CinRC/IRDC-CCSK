import me.gmx.RCCS;
import me.gmx.parser.CCSParser;
import me.gmx.process.ProcessContainer;
import me.gmx.process.nodes.Label;
import me.gmx.process.nodes.LabelFactory;
import me.gmx.process.nodes.TauLabelNode;
import me.gmx.process.process.Process;
import org.junit.jupiter.api.Test;

public class RestrictionTest {

    @Test
    public void testRestrictionSync(){
        RCCS.config.clear();
        Label a,oa,ta;
        a = LabelFactory.createDebugLabel("a");
        oa = LabelFactory.createDebugLabel("'a");
        ta = new TauLabelNode(a,oa);
        Process p = CCSParser.parseLine("(a|'a)\\{a}").export();

        ProcessContainer pc = new ProcessContainer(p);

        System.out.printf("Asserting that %s cannot act on %s...\n", pc.prettyString(), a);
        assert(!pc.canAct(a));
        System.out.printf("Asserting that %s cannot act on %s...\n", pc.prettyString(), oa);
        assert(!pc.canAct(oa));
        System.out.printf("Asserting that %s can act on %s...\n", pc.prettyString(), ta);
        assert(pc.canAct(ta));
    }

}
