package de.fhg.iais.roberta.ast.syntax.sensor.bob3;

import org.junit.Test;

import de.fhg.iais.roberta.util.RobertaProperties;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.util.test.ardu.HelperBob3ForTest;

public class LightSensorTest {

    HelperBob3ForTest h = new HelperBob3ForTest(new RobertaProperties(Util1.loadProperties(null)));

    @Test
    public void getAmbientLight() throws Exception {
        final String a = "double item; void setup() {Serial.begin(9600); item=0;} void loop() {item=myBob.getIRLight();";

        this.h.assertCodeIsOk(a, "/ast/sensors/sensor_bob3AmbientLight.xml", false);
    }
}
