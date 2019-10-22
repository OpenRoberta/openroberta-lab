package de.fhg.iais.roberta.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.Ev3LejosAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class MotorStopActionTest extends Ev3LejosAstTest {

    @Test
    public void stopMotor() throws Exception {
        String a = "\nhal.stopRegulatedMotor(ActorPort.A, MotorStopMode.FLOAT);}";

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(
                testFactory,
                a,
                "/syntax/actions/action_MotorStop.xml",
                makeRotateRegulatedUnregulatedForwardBackwardMotors(),
                false);
    }
}