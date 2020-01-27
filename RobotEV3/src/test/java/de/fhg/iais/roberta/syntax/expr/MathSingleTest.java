package de.fhg.iais.roberta.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.Ev3LejosAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class MathSingleTest extends Ev3LejosAstTest {

    @Test
    public void Test() throws Exception {
        String a =
            "float___Element=(float)Math.sqrt(0);float___Element2=(float)Math.abs(0);float___Element3=-(0);"
                + "float___Element4=(float)Math.log(0);float___Element5=(float)Math.log10(0);float___Element6=(float)Math.exp(0);float___Element7=(float)Math.pow(10,0);"
            		+ "float___Element8=(float)Math.pow(0,2);publicvoidrun()throwsException{}";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/math/math_single.xml", false);
    }
}
