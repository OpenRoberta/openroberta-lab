package de.fhg.iais.roberta.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.Ev3LejosAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class MathConstantTest extends Ev3LejosAstTest {

    @Test
    public void Test() throws Exception {
        String a = "(float)Math.PI(float)Math.E(float)((1.0+Math.sqrt(5.0))/2.0)(float)Math.sqrt(2)" + "(float)Math.sqrt(0.5)Float.POSITIVE_INFINITY}";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/math/math_constant.xml", false);
    }

    @Test
    public void Test1() throws Exception {
        String a = "hal.rotateRegulatedMotor(ActorPort.B,(float)Math.PI,MotorMoveMode.ROTATIONS,(float)((1.0+Math.sqrt(5.0))/2.0));}";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/math/math_constant1.xml", makeStandard(), false);
    }

}
