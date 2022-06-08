import me.gmx.parser.CCSParser;
import me.gmx.process.nodes.Label;
import me.gmx.process.nodes.LabelNode;
import me.gmx.process.process.Process;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ActionTest {

    //Currently bugged. I don't know why this is failing, because the lists
    //are actually the same at runtime, but the assertion is not working properly
    @Test
    public void testBasicActions(){
        HashMap<Process, Collection<Label>> canAct = new HashMap<>();
        canAct.put(CCSParser.parseLine("a").export(),
                Set.of(new LabelNode("a")));
        canAct.put(CCSParser.parseLine("a+b").export(),
                Set.of(new LabelNode("a"), new LabelNode("b")));
        canAct.put(CCSParser.parseLine("a|b+c").export(),
                Set.of(new LabelNode("a"), new LabelNode("b"), new LabelNode("c")));

        for (Map.Entry<Process, Collection<Label>> e: canAct.entrySet()){
            Collection<Label> zz = e.getKey().getActionableLabels();
            assert (zz.containsAll(e.getValue()));
        }
    }



}
