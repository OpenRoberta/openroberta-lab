package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.util.RobertaProperties;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.util.test.nxt.HelperNxtForTest;

public class GetPowerActionTest {
    HelperNxtForTest h = new HelperNxtForTest(new RobertaProperties(Util1.loadProperties(null)));

    @Test
    public void getSpeed() throws Exception {
        final String a = "\nMotorPower(OUT_B)";

        this.h.assertCodeIsOk(a, "/ast/actions/action_MotorGetPower.xml");
    }
}
