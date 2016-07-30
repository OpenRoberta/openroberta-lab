package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.testutil.Helper;

public class ShowTextActionTest {
    @Test
    public void clearDisplay() throws Exception {
        final String a = "\nTextOut(0,LCD_LINE0,\"Hallo\");";

        Helper.assertCodeIsOk(a, "/ast/actions/action_ShowText.xml");

    }
}
