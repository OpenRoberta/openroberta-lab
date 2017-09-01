package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.nxt.Helper;

public class TurnActionTest {

    Helper h = new Helper();

    @Test
    public void turn() throws Exception {
        final String a = "__speed=abs(50)<100?50:50/abs(50)*100;OnFwdSync(OUT_BC,__speed, -100);";

        this.h.assertCodeIsOk(a, "/ast/actions/action_MotorDiffTurn.xml");
    }

    @Test
    public void turnFor() throws Exception {
        final String a =
            "__speed=abs(50)<100?50:50/abs(50)*100;RotateMotorEx(OUT_BC,__speed, ( 20 * TRACKWIDTH / WHEELDIAMETER), -100, true, true );\nWait( 1 );";

        this.h.assertCodeIsOk(a, "/ast/actions/action_MotorDiffTurnFor.xml");
    }
}