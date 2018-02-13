package de.fhg.iais.roberta.ast.syntax.sensors;

import org.junit.Ignore;

import de.fhg.iais.roberta.util.test.nxt.HelperNxtForXmlTest;

public class LightSensorTest {
    private final HelperNxtForXmlTest h = new HelperNxtForXmlTest();

    @Ignore
    public void setColor() throws Exception {
        final String a = "\nSensorColor(IN_3,\"RED\")SensorColor(IN_4,\"AMBIENTLIGHT\")";

        this.h.assertCodeIsOk(a, "/ast/sensors/sensor_setLight.xml");
    }
}
