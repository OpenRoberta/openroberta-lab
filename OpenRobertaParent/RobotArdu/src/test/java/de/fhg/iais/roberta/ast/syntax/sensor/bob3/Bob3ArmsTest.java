package de.fhg.iais.roberta.ast.syntax.sensor.bob3;

import org.junit.Test;

import de.fhg.iais.roberta.util.RobertaProperties;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.util.test.ardu.HelperBob3ForTest;

public class Bob3ArmsTest {

    HelperBob3ForTest h = new HelperBob3ForTest(new RobertaProperties(Util1.loadProperties(null)));

    @Test
    public void getLeftArmRightArmLight() throws Exception {
        final String a = "void setup(){Serial.begin(9600);}void loop(){if((myBob.getArm(1)>0)){}elseif((myBob.getArm(2)==1)){}";

        this.h.assertCodeIsOk(a, "/ast/sensors/sensor_bob3Arms.xml", false);
    }
}
