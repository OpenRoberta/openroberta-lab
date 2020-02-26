package de.fhg.iais.roberta.ast.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.NxtAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class MathRandomIntTest extends NxtAstTest {

    @Test
    public void Test() throws Exception {
        final String a = "Random(100-1)+1";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/math/math_random_int.xml", false);
    }
}
