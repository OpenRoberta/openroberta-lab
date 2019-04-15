package de.fhg.iais.roberta.ast.syntax.sensor.bob3;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.ardu.HelperBob3ForXmlTest;

public class Bob3ArmsTest {

    private final HelperBob3ForXmlTest h = new HelperBob3ForXmlTest();

    @Test
    public void getLeftArmRightArmLight() throws Exception {
        final String a = "void setup(){}void loop(){if((rob.getArm(1)>0)){}elseif((rob.getArm(2)==1)){}}";

        this.h.assertCodeIsOk(a, "/ast/sensors/sensor_bob3Arms.xml", false);
    }
}
