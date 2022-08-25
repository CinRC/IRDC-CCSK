import me.gmx.RCCS;
import me.gmx.parser.CCSParser;
import me.gmx.process.nodes.Label;
import me.gmx.process.nodes.LabelFactory;
import me.gmx.process.nodes.LabelNode;
import me.gmx.process.nodes.TauLabelNode;
import me.gmx.process.process.Process;
import me.gmx.thread.ProcessContainer;
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

        assert(!pc.canAct(a));
        assert(!pc.canAct(oa));
        assert(pc.canAct(ta));
    }

}
