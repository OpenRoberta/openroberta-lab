package de.fhg.iais.roberta.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.Ev3LejosAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class MathNumberPropertyTest extends Ev3LejosAstTest {

    @Test
    public void Test() throws Exception {
        String a =
            "booleanElement=_isPrime((int)0);booleanElement2=(0%1==0);"
                + "booleanElement3=(0%2==0);booleanElement4=(0%2==1);booleanElement5=(0 > 0);"
                + "booleanElement6=(0%0==0);"
                + "booleanElement7=(0 < 0);publicvoidrun()throwsException{}";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/math/math_number_property.xml", false);
    }
}
