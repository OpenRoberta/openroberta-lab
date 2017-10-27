package de.fhg.iais.roberta.ast.syntax.sensor.bob3;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.ardu.HelperBob3;

public class Bob3ArmsTest {

    HelperBob3 h = new HelperBob3();

    @Test
    public void getLeftArmRightArmLight() throws Exception {
        final String a = "void setup(){Serial.begin(9600);}void loop(){if((myBob.getArm(1)==1)){}elseif((myBob.getArm(2)==1)){}";

        this.h.assertCodeIsOk(a, "/ast/sensors/sensor_bob3Arms.xml", false);
    }
}
