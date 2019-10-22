package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.NxtAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class GetPowerActionTest extends NxtAstTest {

    @Test
    public void getSpeed() throws Exception {
        final String a = "\nMotorPower(OUT_B)";

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/ast/actions/action_MotorGetPower.xml", brickConfiguration, false);
    }
}
