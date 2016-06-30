package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.testutil.ev3.Helper;

public class TurnActionTest {

    @Test
    public void turn() throws Exception {
        String a = "\nhal.rotateDirectionRegulated(TurnDirection.RIGHT, 50);";

        Helper.assertCodeIsOk(a, "/syntax/actions/action_MotorDiffTurn.xml");
    }

    @Test
    public void turnFor() throws Exception {
        String a = "\nhal.rotateDirectionAngle(TurnDirection.RIGHT, 50, 20);";

        Helper.assertCodeIsOk(a, "/syntax/actions/action_MotorDiffTurnFor.xml");
    }
}