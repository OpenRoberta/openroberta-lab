package de.fhg.iais.roberta.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.Ev3LejosAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class MathModuloTest extends Ev3LejosAstTest {

    @Test
    public void Test() throws Exception {
        String a = "floatvariablenName=1%0;publicvoidrun()throwsException{}";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/math/math_modulo.xml", false);
    }

}
