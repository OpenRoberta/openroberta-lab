package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.syntax.codegen.arduino.botnroll.BotnrollAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class MotorStopActionTest extends BotnrollAstTest {

    @Test
    public void stopMotor() throws Exception {
        final String a = "one.stop1m(2)\n";

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/ast/actions/action_MotorStop.xml", makeConfiguration(), false);
    }
}