package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.util.RobertaProperties;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.util.test.nxt.HelperNxtForTest;

public class TurnActionTest {

    HelperNxtForTest h = new HelperNxtForTest(new RobertaProperties(Util1.loadProperties(null)));

    @Test
    public void turn() throws Exception {
        final String a = "OnFwdSync(OUT_BC,SpeedTest(50), -100);";

        this.h.assertCodeIsOk(a, "/ast/actions/action_MotorDiffTurn.xml");
    }

    @Test
    public void turnFor() throws Exception {
        final String a = "RotateMotorEx(OUT_BC,SpeedTest(50), ( 20 * TRACKWIDTH / WHEELDIAMETER), -100, true, true );\nWait( 1 );";

        this.h.assertCodeIsOk(a, "/ast/actions/action_MotorDiffTurnFor.xml");
    }
}