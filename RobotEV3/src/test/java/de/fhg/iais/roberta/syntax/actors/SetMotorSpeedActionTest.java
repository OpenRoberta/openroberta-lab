package de.fhg.iais.roberta.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.Ev3LejosAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class SetMotorSpeedActionTest extends Ev3LejosAstTest {

    @Test
    public void setMotorSpeed() throws Exception {
        String a = "\nhal.setRegulatedMotorSpeed(ActorPort.B, 30);}";

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/actions/action_MotorSetPower.xml", makeStandard(), false);
    }
}