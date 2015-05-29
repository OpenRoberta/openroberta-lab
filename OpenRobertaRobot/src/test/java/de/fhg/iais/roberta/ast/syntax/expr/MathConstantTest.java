package de.fhg.iais.roberta.ast.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.testutil.Helper;

public class MathConstantTest {
    @Test
    public void Test() throws Exception {
        String a = "Math.PIMath.EBlocklyMethods.GOLDEN_RATIOMath.sqrt(2)Math.sqrt(1.0/2.0)Double.POSITIVE_INFINITY";

        Helper.assertCodeIsOk(a, "/syntax/math/math_constant.xml");
    }

    @Test
    public void Test1() throws Exception {
        String a = "hal.rotateRegulatedMotor(ActorPort.B,Math.PI,MotorMoveMode.ROTATIONS,BlocklyMethods.GOLDEN_RATIO);";

        Helper.assertCodeIsOk(a, "/syntax/math/math_constant1.xml");
    }

}
