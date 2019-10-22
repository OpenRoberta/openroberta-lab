package de.fhg.iais.roberta.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.Ev3LejosAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class MathRandomIntTest extends Ev3LejosAstTest {

    @Test
    public void Test() throws Exception {
        String a = "Math.round(Math.random()*(100-1))+1}";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/math/math_random_int.xml", false);
    }
}
