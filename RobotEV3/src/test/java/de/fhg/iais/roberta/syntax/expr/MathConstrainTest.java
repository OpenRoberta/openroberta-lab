package de.fhg.iais.roberta.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.ev3.Helper;

public class MathConstrainTest {
    Helper h = new Helper();

    @Test
    public void Test() throws Exception {
        String a = "BlocklyMethods.clamp(hal.getUltraSonicSensorDistance(SensorPort.S4),1,100)}";

        this.h.assertCodeIsOk(a, "/syntax/math/math_constrain.xml");
    }
}
