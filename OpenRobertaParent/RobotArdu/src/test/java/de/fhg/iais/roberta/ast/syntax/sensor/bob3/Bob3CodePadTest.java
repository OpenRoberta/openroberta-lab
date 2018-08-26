package de.fhg.iais.roberta.ast.syntax.sensor.bob3;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.ardu.HelperBob3ForXmlTest;

public class Bob3CodePadTest {

    private final HelperBob3ForXmlTest h = new HelperBob3ForXmlTest();

    @Test
    public void getLeftArmRightArmLight() throws Exception {
        final String a = "double item; void setup(){ Serial.begin(9600); item = 0; } void loop() { item = myBob.getID();}";

        this.h.assertCodeIsOk(a, "/ast/sensors/sensor_bob3CodePad.xml", false);
    }
}
