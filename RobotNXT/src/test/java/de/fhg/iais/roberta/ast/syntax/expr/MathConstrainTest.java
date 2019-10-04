package de.fhg.iais.roberta.ast.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.NxtAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class MathConstrainTest extends NxtAstTest {

    @Test
    public void Test() throws Exception {
        final String a = "MIN(MAX(SensorUS(S4),1),100)";

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/math/math_constrain.xml", brickConfigurationUS2US4, false);
    }
}
