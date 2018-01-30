package de.fhg.iais.roberta.ast.syntax.sensors;

import org.junit.Test;

import de.fhg.iais.roberta.util.RobertaProperties;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.util.test.nxt.HelperNxtForTest;

public class BrickSensorTest {
    HelperNxtForTest h = new HelperNxtForTest(new RobertaProperties(Util1.loadProperties(null)));

    @Test
    public void isPressed() throws Exception {
        String a = "\nButtonPressed(BTNCENTER,false)";

        this.h.assertCodeIsOk(a, "/ast/sensors/sensor_brick1.xml");
    }
}
