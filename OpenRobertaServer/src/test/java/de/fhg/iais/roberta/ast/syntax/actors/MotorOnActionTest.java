package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.codegen.lejos.Helper;

public class MotorOnActionTest {

    @Test
    public void motorOn() throws Exception {
        String a = "hal.turnOnRegulatedMotor(ActorPort.B, 30);" + "hal.turnOnUnregulatedMotor(ActorPort.C, 50);";

        Helper.assertCodeIsOk(a, "/ast/actions/action_MotorOn.xml");
    }

    @Test
    public void motorOnFor() throws Exception {
        String a = "hal.rotateRegulatedMotor(ActorPort.B, 30, MotorMoveMode.ROTATIONS, 1);";

        Helper.assertCodeIsOk(a, "/ast/actions/action_MotorOnFor.xml");
    }
}