package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.testutil.Helper;

public class LightActionStatusTest {
    @Test
    public void ledOff() throws Exception {
        String a = "\nhal.ledOff();";

        Helper.assertCodeIsOk(a, "/ast/actions/action_BrickLightStatus.xml");
    }

    @Test
    public void resetLED() throws Exception {
        String a = "\nhal.resetLED();";

        Helper.assertCodeIsOk(a, "/ast/actions/action_BrickLightStatus1.xml");
    }
}
