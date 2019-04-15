package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.nxt.HelperNxtForXmlTest;

public class ShowTextActionTest {
    private final HelperNxtForXmlTest h = new HelperNxtForXmlTest();

    @Test
    public void clearDisplay() throws Exception {
        final String a = "\nTextOut(0,(MAXLINES - 0) * MAXLINES,\"Hallo\");";

        this.h.assertCodeIsOk(a, "/ast/actions/action_ShowText.xml");
    }

}
