package de.fhg.iais.roberta.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.Ev3LejosAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class MathConstrainTest extends Ev3LejosAstTest {

    @Test
    public void Test() throws Exception {
        String a = "Math.min(Math.max(hal.getUltraSonicSensorDistance(SensorPort.S4),1),100)}";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/math/math_constrain.xml", makeStandard(), false);
    }
}
