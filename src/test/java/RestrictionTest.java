import me.gmx.RCCS;
import me.gmx.parser.CCSParser;
import me.gmx.process.nodes.Label;
import me.gmx.process.nodes.LabelFactory;
import me.gmx.process.nodes.LabelNode;
import me.gmx.process.nodes.TauLabelNode;
import me.gmx.thread.ProcessContainer;
import org.junit.jupiter.api.Test;

public class RestrictionTest {

    @Test
    public void testRestriction(){
        RCCS.config.clear();

        //Create debug labels
        Label a,oa, b;
        a = LabelFactory.createDebugLabel("a");
        oa = LabelFactory.createDebugLabel("'a");
        b = LabelFactory.createDebugLabel("b");

        ProcessContainer c = new ProcessContainer(CCSParser.parseLine("a\\{a}").export());
        assert !c.canAct(a);

        c = new ProcessContainer(CCSParser.parseLine("(a|'a)\\{a}").export());
        //Make sure process isn't wack
        assert !c.canAct(new TauLabelNode(a,b));
        assert !c.canAct(b);
        //Make sure restrictions work
        assert !c.canAct(a);
        //Make sure restrictions don't overstep
        assert c.canAct(oa);
        assert c.canAct(new TauLabelNode(a,oa));

    }

}
