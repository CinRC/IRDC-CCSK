package tests;

import org.cinrc.IRDC;
import org.cinrc.parser.CCSParser;
import org.cinrc.parser.CCSParserException;
import org.cinrc.process.ProcessContainer;
import org.cinrc.process.nodes.Label;
import org.cinrc.process.nodes.LabelFactory;
import org.cinrc.process.process.Process;
import org.cinrc.process.process.SummationProcess;
import org.junit.jupiter.api.Test;

public class RegenerationTest {

  @Test
  public void testPrecedence() {
    IRDC.config.clear();
    boolean failed = false;
    try {
      Process p = new CCSParser().parseLine("a[k0]|b[k0]");
    }catch(CCSParserException cpe){
      failed = true;
    }
    assert failed;

  }
}
