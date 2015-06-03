package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.codegen.lejos.Helper;

public class MotorDriveStopActionTest {

    @Test
    public void stop() throws Exception {
        String a = "\nhal.stopRegulatedDrive(ActorPort.A, ActorPort.B);";

        Helper.assertCodeIsOk(a, "/ast/actions/action_Stop.xml");
    }
}