package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Ignore;
import org.junit.Test;

import de.fhg.iais.roberta.util.test.ardu.HelperBotNrollForXmlTest;

@Ignore
public class ClearDisplayActionTest {
    private final HelperBotNrollForXmlTest h = new HelperBotNrollForXmlTest();

    @Test
    public void clearDisplay() throws Exception {
        String a = "publicvoidrun(){clearscreen();}";

        this.h.assertCodeIsOk(a, "/ast/actions/action_ClearDisplay.xml", false);
    }
}