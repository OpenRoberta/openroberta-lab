package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.testutil.Helper;

public class ClearDisplayActionTest {
    @Test
    public void clearDisplay() throws Exception {
        String a = "publicvoidrun(){hal.clearDisplay();}";

        Helper.assertCodeIsOk(a, "/ast/actions/action_ClearDisplay.xml");
    }
}
