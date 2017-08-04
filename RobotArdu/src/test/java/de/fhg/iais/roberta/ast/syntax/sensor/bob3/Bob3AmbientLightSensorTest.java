package de.fhg.iais.roberta.ast.syntax.sensor.bob3;

import org.junit.Ignore;
import org.junit.Test;

import de.fhg.iais.roberta.util.test.ardu.HelperBob3;

@Ignore
public class Bob3AmbientLightSensorTest {

    HelperBob3 h = new HelperBob3();

    @Test
    public void getAmbientLight() throws Exception {
        final String a = "myBob.getIRSensor()";

        this.h.assertCodeIsOk(a, "/ast/sensors/sensor_bob3AmbientLight.xml", false);
    }
}
