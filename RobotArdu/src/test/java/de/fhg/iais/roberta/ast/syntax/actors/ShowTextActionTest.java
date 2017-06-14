package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.ardu.HelperBotNroll;

public class ShowTextActionTest {

    HelperBotNroll h = new HelperBotNroll();

    @Test
    public void clearDisplay() throws Exception {
        final String a = "\none.lcd1(\"Hallo\");";

        this.h.assertCodeIsOk(a, "/ast/actions/action_ShowText.xml", false);

    }
}
