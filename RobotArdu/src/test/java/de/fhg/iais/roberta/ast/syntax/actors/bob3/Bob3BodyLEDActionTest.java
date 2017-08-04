package de.fhg.iais.roberta.ast.syntax.actors.bob3;

import org.junit.Ignore;
import org.junit.Test;

import de.fhg.iais.roberta.util.test.ardu.HelperBob3;

@Ignore
public class Bob3BodyLEDActionTest {

    HelperBob3 h = new HelperBob3();

    @Test
    public void turnOnLeftLED() throws Exception {
        final String a = "myBob.setLed(LED_4, ON);";

        this.h.assertCodeIsOk(a, "/ast/actions/action_Bob3TurnLeftLEDOn.xml", false);
    }

    @Test
    public void turnOffLeftLED() throws Exception {
        final String a = "myBob.setLed(LED_4, OFF);";

        this.h.assertCodeIsOk(a, "/ast/actions/action_Bob3TurnLeftLEDOff.xml", false);
    }

    @Test
    public void turnOnRightLED() throws Exception {
        final String a = "myBob.setLed(LED_3, ON);";

        this.h.assertCodeIsOk(a, "/ast/actions/action_Bob3TurnRightLEDOn.xml", false);
    }

    @Test
    public void turnOffRightLED() throws Exception {
        final String a = "myBob.setLed(LED_3, OFF);";

        this.h.assertCodeIsOk(a, "/ast/actions/action_Bob3TurnRightLEDOff.xml", false);
    }
}
