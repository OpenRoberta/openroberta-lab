package de.fhg.iais.roberta.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.testutil.Helper;

public class LightActionTest {
    @Test
    public void ledOn() throws Exception {
        String a = "\nhal.ledOn(BrickLedColor.GREEN, BlinkMode.ON);}";

        Helper.assertCodeIsOk(a, "/syntax/actions/action_BrickLight.xml");
    }
}
