package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.ardu.HelperBotNrollForXmlTest;

public class ShowTextActionTest {

    private final HelperBotNrollForXmlTest h = new HelperBotNrollForXmlTest();

    @Test
    public void clearDisplay() throws Exception {
        final String a = "\none.lcd1(\"Hallo\");";

        this.h.assertCodeIsOk(a, "/ast/actions/action_ShowText.xml", false);

    }
}
