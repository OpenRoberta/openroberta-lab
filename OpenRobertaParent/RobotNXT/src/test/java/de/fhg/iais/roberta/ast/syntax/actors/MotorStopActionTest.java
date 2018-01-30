package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.util.RobertaProperties;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.util.test.nxt.HelperNxtForTest;

public class MotorStopActionTest {
    HelperNxtForTest h = new HelperNxtForTest(new RobertaProperties(Util1.loadProperties(null)));

    @Test
    public void stopMotor() throws Exception {
        final String a = "\nFloat(OUT_A);";

        this.h.assertCodeIsOk(a, "/ast/actions/action_MotorStop.xml");
    }
}