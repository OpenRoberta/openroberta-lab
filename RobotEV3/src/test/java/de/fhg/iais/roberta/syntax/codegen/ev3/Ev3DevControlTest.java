package de.fhg.iais.roberta.syntax.codegen.ev3;

import org.junit.Test;

import de.fhg.iais.roberta.Ev3DevAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class Ev3DevControlTest extends Ev3DevAstTest {

    @Test
    public void ev3DevWaitTest() throws Exception {
        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXml(
                testFactory,
                "/ast/control" + "/ev3dev_wait_test.py",
                "/ast/control" + "/ev3dev_wait_test.xml",
                makeStandard());
    }
}
