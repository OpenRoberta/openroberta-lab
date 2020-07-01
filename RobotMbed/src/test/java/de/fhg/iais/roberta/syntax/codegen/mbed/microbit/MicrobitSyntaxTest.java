package de.fhg.iais.roberta.syntax.codegen.mbed.microbit;

import org.junit.Test;

import de.fhg.iais.roberta.syntax.MicrobitAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class MicrobitSyntaxTest extends MicrobitAstTest {

    @Test(expected = AssertionError.class)
    public void emptyValuesTest() throws Exception {
        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXml(
                testFactory,
                "/stmts/microbit_emtpy_values_test.py",
                "/stmts/microbit_emtpy_values_test.xml",
                configuration);
    }

    @Test
    public void waitTimeConditionTest() throws Exception {
        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXml(testFactory, "/stmts/microbit_wait_test.py", "/stmts/microbit_wait_test.xml", configuration);
    }
}
