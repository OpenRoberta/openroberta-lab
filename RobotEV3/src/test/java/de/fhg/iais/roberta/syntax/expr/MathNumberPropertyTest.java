package de.fhg.iais.roberta.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.Ev3LejosAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class MathNumberPropertyTest extends Ev3LejosAstTest {

    @Test
    public void Test() throws Exception {
        String a =
            "boolean___Element=_isPrime((int)0);boolean___Element2=(0%1==0);"
                + "boolean___Element3=(0%2==0);boolean___Element4=(0%2==1);boolean___Element5=(0 > 0);"
                + "boolean___Element6=(0%0==0);"
                + "boolean___Element7=(0 < 0);publicvoidrun()throwsException{}";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/math/math_number_property.xml", false);
    }
}
