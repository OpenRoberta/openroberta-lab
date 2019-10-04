package de.fhg.iais.roberta.ast.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.NxtAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class MathTrigTest extends NxtAstTest {

    @Test
    public void Test() throws Exception {
        final String a = "MathSin(0)MathCos(0)MathTan(0)MathAsin(0)MathAcos(0)MathAtan(0)";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/math/math_trig.xml", brickConfiguration, false);
    }

    @Test
    public void Test1() throws Exception {
        final String a = "if(0==MathSin(0)){OnFwdRegEx(OUT_BC,MIN(MAX(MathAcos(0), -100), 100),OUT_REGMODE_SYNC,RESET_NONE);}";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/math/math_trig1.xml", brickConfigurationBC, false);
    }

}
