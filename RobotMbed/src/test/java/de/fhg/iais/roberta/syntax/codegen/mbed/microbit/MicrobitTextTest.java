package de.fhg.iais.roberta.syntax.codegen.mbed.microbit;

import org.junit.Test;

import de.fhg.iais.roberta.syntax.MicrobitAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class MicrobitTextTest extends MicrobitAstTest {

    @Test
    public void mathOnListsTest() throws Exception {
        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXml(
                testFactory,
                "/function/microbit_text_join_test.py",
                "/function/microbit_text_join_test.xml",
                configuration);
    }

}
