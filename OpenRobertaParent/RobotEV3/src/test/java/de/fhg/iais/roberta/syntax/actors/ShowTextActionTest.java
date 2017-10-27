package de.fhg.iais.roberta.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.ev3.Helper;

public class ShowTextActionTest {
    Helper h = new Helper();

    @Test
    public void clearDisplay() throws Exception {
        String a = "\nhal.drawText(\"Hallo\", 0, 0);}";

        this.h.assertCodeIsOk(a, "/syntax/actions/action_ShowText.xml");

    }
}
