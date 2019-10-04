package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.NxtAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class SetMotorSpeedActionTest extends NxtAstTest {

    @Test
    public void setMotorSpeed() throws Exception {
        final String a = "OnFwdRegEx(OUT_B,MIN(MAX(30, -100), 100),OUT_REGMODE_SPEED,RESET_NONE);";

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/ast/actions/action_MotorSetPower.xml", brickConfiguration, false);
    }
}