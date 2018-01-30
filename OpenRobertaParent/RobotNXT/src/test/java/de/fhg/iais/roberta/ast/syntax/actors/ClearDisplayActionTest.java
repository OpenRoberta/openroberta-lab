package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Ignore;
import org.junit.Test;

import de.fhg.iais.roberta.util.RobertaProperties;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.util.test.nxt.HelperNxtForTest;

@Ignore
public class ClearDisplayActionTest {
    HelperNxtForTest h = new HelperNxtForTest(new RobertaProperties(Util1.loadProperties(null)));

    @Test
    public void clearDisplay() throws Exception {
        String a = "publicvoidrun(){clearscreen();}";

        this.h.assertCodeIsOk(a, "/ast/actions/action_ClearDisplay.xml");
    }
}