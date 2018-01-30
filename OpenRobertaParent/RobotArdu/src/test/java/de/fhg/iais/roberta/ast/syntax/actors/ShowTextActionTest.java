package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.util.RobertaProperties;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.util.test.ardu.HelperBotNrollForTest;

public class ShowTextActionTest {

    HelperBotNrollForTest h = new HelperBotNrollForTest(new RobertaProperties(Util1.loadProperties(null)));

    @Test
    public void clearDisplay() throws Exception {
        final String a = "\none.lcd1(\"Hallo\");";

        this.h.assertCodeIsOk(a, "/ast/actions/action_ShowText.xml", false);

    }
}
