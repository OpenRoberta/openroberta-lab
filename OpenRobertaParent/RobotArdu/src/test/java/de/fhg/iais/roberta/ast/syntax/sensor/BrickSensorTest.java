package de.fhg.iais.roberta.ast.syntax.sensor;

import org.junit.Test;

import de.fhg.iais.roberta.util.RobertaProperties;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.util.test.ardu.HelperBotNrollForTest;

public class BrickSensorTest {
    HelperBotNrollForTest h = new HelperBotNrollForTest(new RobertaProperties(Util1.loadProperties(null)));

    @Test
    public void isPressed() throws Exception {
        String a = "\nbnr.buttonIsPressed(2)";

        this.h.assertCodeIsOk(a, "/ast/sensors/sensor_brick1.xml", false);
    }
}
