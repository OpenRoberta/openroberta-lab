package de.fhg.iais.roberta.ast.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.NxtAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class MathRandomIntTest extends NxtAstTest {

    @Test
    public void Test() throws Exception {
        final String a = "RandomIntegerInRange(1,100)";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/math/math_random_int.xml", false);
    }
}
