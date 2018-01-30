package de.fhg.iais.roberta.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.util.RobertaProperties;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.util.test.ev3.HelperEv3ForTest;

public class TurnActionTest {
    HelperEv3ForTest h = new HelperEv3ForTest(new RobertaProperties(Util1.loadProperties(null)));

    @Test
    public void turn() throws Exception {
        String a = "\nhal.rotateDirectionRegulated(TurnDirection.RIGHT, 50);}";

        this.h.assertCodeIsOk(a, "/syntax/actions/action_MotorDiffTurn.xml");
    }

    @Test
    public void turnFor() throws Exception {
        String a = "\nhal.rotateDirectionAngle(TurnDirection.RIGHT, 50, 20);}";

        this.h.assertCodeIsOk(a, "/syntax/actions/action_MotorDiffTurnFor.xml");
    }
}