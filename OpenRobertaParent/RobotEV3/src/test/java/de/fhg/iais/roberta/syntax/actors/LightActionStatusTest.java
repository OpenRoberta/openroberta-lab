package de.fhg.iais.roberta.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.util.RobertaProperties;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.util.test.ev3.HelperEv3ForTest;

public class LightActionStatusTest {
    HelperEv3ForTest h = new HelperEv3ForTest(new RobertaProperties(Util1.loadProperties(null)));

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
