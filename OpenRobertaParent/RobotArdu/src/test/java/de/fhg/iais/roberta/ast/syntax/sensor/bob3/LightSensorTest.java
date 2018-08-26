package de.fhg.iais.roberta.ast.syntax.sensor.bob3;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.ardu.HelperBob3ForXmlTest;

public class LightSensorTest {

    private final HelperBob3ForXmlTest h = new HelperBob3ForXmlTest();

    @Test
    public void getAmbientLight() throws Exception {
        final String a = "double item; void setup() {Serial.begin(9600); item=0;} void loop() {item=myBob.getIRLight();}";

        this.h.assertCodeIsOk(a, "/ast/sensors/sensor_bob3AmbientLight.xml", false);
    }
}