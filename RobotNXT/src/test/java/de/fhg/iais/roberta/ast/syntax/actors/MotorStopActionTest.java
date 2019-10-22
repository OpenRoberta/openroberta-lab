package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.NxtAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class MotorStopActionTest extends NxtAstTest {

    @Test
    public void stopMotor() throws Exception {
        final String a = "\nFloat(OUT_A);";

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/ast/actions/action_MotorStop.xml", brickConfiguration, false);
    }
}