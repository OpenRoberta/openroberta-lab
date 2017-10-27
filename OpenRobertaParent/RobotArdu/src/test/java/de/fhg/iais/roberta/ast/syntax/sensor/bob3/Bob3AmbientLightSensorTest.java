package de.fhg.iais.roberta.ast.syntax.sensor.bob3;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.ardu.HelperBob3;

public class Bob3AmbientLightSensorTest {

    HelperBob3 h = new HelperBob3();

    @Test
    public void getAmbientLight() throws Exception {
        final String a = "double item; void setup() {Serial.begin(9600); item=0;} void loop() {item=myBob.getIRSensor();";

        this.h.assertCodeIsOk(a, "/ast/sensors/sensor_bob3AmbientLight.xml", false);
    }
}
