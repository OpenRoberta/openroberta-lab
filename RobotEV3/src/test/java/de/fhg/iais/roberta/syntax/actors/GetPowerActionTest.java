package de.fhg.iais.roberta.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.Ev3LejosAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class GetPowerActionTest extends Ev3LejosAstTest {

    @Test
    public void getSpeed() throws Exception {
        String a = "\nhal.getRegulatedMotorSpeed(ActorPort.B)}";

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/actions/action_MotorGetPower.xml", makeStandard(), false);
    }
}
