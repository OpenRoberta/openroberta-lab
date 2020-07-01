package de.fhg.iais.roberta.syntax.codegen.mbed.microbit;

import org.junit.Test;

import de.fhg.iais.roberta.syntax.MicrobitAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class MicrobitListsTest extends MicrobitAstTest {

    @Test
    public void mathOnListsTest() throws Exception {
        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXml(
                testFactory,
                "/lists/microbit_math_on_lists_test.py",
                "/lists/microbit_math_on_lists_test.xml",
                configuration);
    }

    @Test
    public void fullListsTest() throws Exception {
        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXml(
                testFactory,
                "/lists/microbit_lists_full_test.py",
                "/lists/microbit_lists_full_test.xml",
                configuration);
    }

}
