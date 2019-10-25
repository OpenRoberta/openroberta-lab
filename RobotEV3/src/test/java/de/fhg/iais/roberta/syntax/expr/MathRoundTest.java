package de.fhg.iais.roberta.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.Ev3LejosAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class MathRoundTest extends Ev3LejosAstTest {

    @Test
    public void Test() throws Exception {
        String a =
            "float___Element=(float)Math.round(0);float___Element2=(float)Math.floor(0);float___Element3=(float)Math.ceil(0);publicvoidrun()throwsException{}";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/math/math_round.xml", false);
    }
}
