package de.fhg.iais.roberta.ast.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.syntax.codegen.arduino.botnroll.BotnrollAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class MathConstrainTest extends BotnrollAstTest {

    @Test
    public void Test() throws Exception {
        final String a = "std::min(std::max((double) bnr.ultrasonicDistance(4), (double) 1), (double) 100)";

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/math/math_constrain.xml", makeConfiguration(), false);
    }
}
