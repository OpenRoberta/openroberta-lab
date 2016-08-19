package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.testutil.Helper;

public class TurnActionTest {

    @Test
    public void turn() throws Exception {
        final String a = "\n50;one.movePID(speedA,speedB);";

        Helper.assertCodeIsOk(a, "/ast/actions/action_MotorDiffTurn.xml");
    }

    @Test
    public void turnFor() throws Exception {
        final String a = "\n50;RotateMotorExspeedA,speedB);";

        Helper.assertCodeIsOk(a, "/ast/actions/action_MotorDiffTurnFor.xml");
    }
}