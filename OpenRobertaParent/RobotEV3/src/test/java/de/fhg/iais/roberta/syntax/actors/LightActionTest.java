package de.fhg.iais.roberta.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.ev3.Helper;

public class LightActionTest {
    Helper h = new Helper();

    @Test
    public void ledOn() throws Exception {
        String a = "\nhal.ledOn(BrickLedColor.GREEN, BlinkMode.ON);}";

        this.h.assertCodeIsOk(a, "/syntax/actions/action_BrickLight.xml");
    }
}
