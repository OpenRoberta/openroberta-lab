package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.testutil.Helper;

public class TurnActionTest {

    @Test
    public void turn() throws Exception {
        final String a = "\nspeedA=50;speedB=-50;one.movePID(speedA,speedB);";

        Helper.assertCodeIsOk(a, "/ast/actions/action_MotorDiffTurn.xml");
    }

    @Test
    public void turnFor() throws Exception {
        final String a = "\nspeedA=50;speedB=-50;RotateMotorExspeedA,speedB);";

        Helper.assertCodeIsOk(a, "/ast/actions/action_MotorDiffTurnFor.xml");
    }
}