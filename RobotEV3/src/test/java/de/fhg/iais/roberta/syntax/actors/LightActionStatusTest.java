package de.fhg.iais.roberta.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.ev3.HelperEv3ForXmlTest;

public class LightActionStatusTest {
    private final HelperEv3ForXmlTest h = new HelperEv3ForXmlTest();

    @Test
    public void ledOff() throws Exception {
        String a = "\nhal.ledOff();}";

        this.h.assertCodeIsOk(a, "/syntax/actions/action_BrickLightStatus.xml");
    }

    @Test
    public void resetLED() throws Exception {
        String a = "\nhal.resetLED();}";

        this.h.assertCodeIsOk(a, "/syntax/actions/action_BrickLightStatus1.xml");
    }
}
