package de.fhg.iais.roberta.ast.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.NxtAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class MathSingleTest extends NxtAstTest {

    @Test
    public void Test() throws Exception {
        final String a = "sqrt(0)abs(0)-(0)MathLn(0)MathLog(0)MathPow(M_E,0)MathPow(10,0)";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/math/math_single.xml", false);
    }

    @Test
    public void Test1() throws Exception {
        final String a = "volume=(sqrt(0))*4/100.0;";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/math/math_single1.xml", false);
    }

    @Test
    public void Test2() throws Exception {
        final String a = "___item=sqrt(0);";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/math/math_single2.xml", false);
    }
}
