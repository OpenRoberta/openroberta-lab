package de.fhg.iais.roberta.ast.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.NxtAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class MathRoundTest extends NxtAstTest {

    @Test
    public void Test() throws Exception {
        final String a = "MathRound(0)MathRoundUp(0)MathFloor(0)";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/math/math_round.xml", false);
    }
}
