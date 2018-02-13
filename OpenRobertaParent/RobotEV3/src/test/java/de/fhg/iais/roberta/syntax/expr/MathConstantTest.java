package de.fhg.iais.roberta.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.ev3.HelperEv3ForXmlTest;

public class MathConstantTest {
    private final HelperEv3ForXmlTest h = new HelperEv3ForXmlTest();

    @Test
    public void Test() throws Exception {
        String a = "BlocklyMethods.PIBlocklyMethods.EBlocklyMethods.GOLDEN_RATIOBlocklyMethods.sqrt(2)BlocklyMethods.sqrt(1.0/2.0)Float.POSITIVE_INFINITY}";

        this.h.assertCodeIsOk(a, "/syntax/math/math_constant.xml");
    }

    @Test
    public void Test1() throws Exception {
        String a = "hal.rotateRegulatedMotor(ActorPort.B,BlocklyMethods.PI,MotorMoveMode.ROTATIONS,BlocklyMethods.GOLDEN_RATIO);}";

        this.h.assertCodeIsOk(a, "/syntax/math/math_constant1.xml");
    }

}
