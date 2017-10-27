package de.fhg.iais.roberta.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.ev3.Helper;

public class ClearDisplayActionTest {
    Helper h = new Helper();

    @Test
    public void clearDisplay() throws Exception {
        String a = "publicvoidrun()throwsException{hal.clearDisplay();}";

        this.h.assertCodeIsOk(a, "/syntax/actions/action_ClearDisplay.xml");
    }
}
