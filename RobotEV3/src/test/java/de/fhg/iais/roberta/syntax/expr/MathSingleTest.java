package de.fhg.iais.roberta.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.Ev3LejosAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class MathSingleTest extends Ev3LejosAstTest {

    @Test
    public void Test() throws Exception {
        String a =
            "floatElement=(float)Math.sqrt(0);floatElement2=(float)Math.abs(0);floatElement3=-(0);"
                + "floatElement4=(float)Math.log(0);floatElement5=(float)Math.log10(0);floatElement6=(float)Math.exp(0);floatElement7=(float)Math.pow(10,0);publicvoidrun()throwsException{}";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/math/math_single.xml", false);
    }
}
