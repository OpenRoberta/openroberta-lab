package de.fhg.iais.roberta.ast.syntax.sensors;

import org.junit.Ignore;

import de.fhg.iais.roberta.util.test.nxt.HelperNxtForXmlTest;

public class SoundSensorTest {
    private final HelperNxtForXmlTest h = new HelperNxtForXmlTest();

    @Ignore
    public void setColor() throws Exception {
        final String a = "\nSensorColor(IN_2)";

        this.h.assertCodeIsOk(a, "/ast/sensors/sensor_setLight.xml");
    }
}
