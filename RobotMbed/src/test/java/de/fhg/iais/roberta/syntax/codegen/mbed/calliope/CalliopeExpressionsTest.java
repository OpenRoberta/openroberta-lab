package de.fhg.iais.roberta.syntax.codegen.mbed.calliope;

import org.junit.Test;

import de.fhg.iais.roberta.syntax.CalliopeAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class CalliopeExpressionsTest extends CalliopeAstTest {

    @Test
    public void calliopeBinaryTest() throws Exception {
        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXml(testFactory, "/expr/calliope_binary_test.cpp", "/expr/calliope_binary_test.xml", configuration);
    }

}
