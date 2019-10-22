package de.fhg.iais.roberta.syntax.codegen.arduino.bob3;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class Bob3MathTest extends Bob3AstTest {

    @Test
    public void clampTest() throws Exception {
        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXml(testFactory, "/ast/math/bob3_clamp_test.ino", "/ast/math/bob3_clamp_test.xml");
    }

}
