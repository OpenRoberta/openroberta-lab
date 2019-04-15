package de.fhg.iais.roberta.ast.syntax.sensor;

import org.junit.Ignore;
import org.junit.Test;

import de.fhg.iais.roberta.util.test.ardu.HelperBotNrollForXmlTest;

@Ignore // TODO: reactivate this test REFACTORING
public class BrickSensorTest {
    private final HelperBotNrollForXmlTest h = new HelperBotNrollForXmlTest();

    @Test
    public void isPressed() throws Exception {
        String a = "\nbnr.buttonIsPressed(2)";

        this.h.assertCodeIsOk(a, "/ast/sensors/sensor_brick1.xml", false);
    }
}
