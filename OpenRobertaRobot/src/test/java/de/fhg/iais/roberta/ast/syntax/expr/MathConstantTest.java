package de.fhg.iais.roberta.ast.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.testutil.Helper;

public class MathConstantTest {
    @Test
    public void Test() throws Exception {
        String a = "BlocklyMethods.PIBlocklyMethods.EBlocklyMethods.GOLDEN_RATIOBlocklyMethods.sqrt(2)BlocklyMethods.sqrt(1.0/2.0)Float.POSITIVE_INFINITY";

        Helper.assertCodeIsOk(a, "/syntax/math/math_constant.xml");
    }

    @Test
    public void Test1() throws Exception {
        String a = "hal.rotateRegulatedMotor(ActorPort.B,BlocklyMethods.PI,MotorMoveMode.ROTATIONS,BlocklyMethods.GOLDEN_RATIO);";

        Helper.assertCodeIsOk(a, "/syntax/math/math_constant1.xml");
    }

}
