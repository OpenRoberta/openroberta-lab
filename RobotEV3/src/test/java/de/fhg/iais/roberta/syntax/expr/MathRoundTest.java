package de.fhg.iais.roberta.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.Ev3LejosAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class MathRoundTest extends Ev3LejosAstTest {

    @Test
    public void Test() throws Exception {
        String a = "floatElement=Math.round(0);floatElement2=Math.floor(0);floatElement3=Math.ceil(0);publicvoidrun()throwsException{}";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/math/math_round.xml", false);
    }
}
