package de.fhg.iais.roberta.ast.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.syntax.codegen.arduino.botnroll.BotnrollAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class MathRandomIntTest extends BotnrollAstTest {

    @Test
    public void Test() throws Exception {
        final String a = "_randomIntegerInRange(1,100)";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/math/math_random_int.xml", false);
    }
}
