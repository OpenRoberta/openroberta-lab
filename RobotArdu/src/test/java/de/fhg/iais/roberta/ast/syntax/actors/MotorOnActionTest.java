package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.testutil.Helper;

public class MotorOnActionTest {

    @Test
    public void motorOn() throws Exception {
        String a = "speedB=30;one.movePID(speedA,speedB);speedC=50;one.movePID(speedA,speedB);";

        Helper.assertCodeIsOk(a, "/ast/actions/action_MotorOn.xml");
    }

    @Test
    public void motorOnFor() throws Exception {
        String a = "speedB=30;one.moveMotorRotation(speedA,speedB,1);";

        Helper.assertCodeIsOk(a, "/ast/actions/action_MotorOnFor.xml");
    }
}