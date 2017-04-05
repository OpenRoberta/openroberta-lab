package de.fhg.iais.roberta.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.testutil.Helper;

public class ShowTextActionTest {
    @Test
    public void clearDisplay() throws Exception {
        String a = "\nhal.drawText(\"Hallo\", 0, 0);}";

        Helper.assertCodeIsOk(a, "/syntax/actions/action_ShowText.xml");

    }
}
