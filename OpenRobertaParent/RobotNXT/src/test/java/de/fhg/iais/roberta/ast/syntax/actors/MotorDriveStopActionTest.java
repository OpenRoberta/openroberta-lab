package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.util.RobertaProperties;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.util.test.nxt.HelperNxtForTest;

public class MotorDriveStopActionTest {
    HelperNxtForTest h = new HelperNxtForTest(new RobertaProperties(Util1.loadProperties(null)));

    @Test
    public void stop() throws Exception {
        final String a = "\nOff(OUT_BC);";

        this.h.assertCodeIsOk(a, "/ast/actions/action_Stop.xml");
    }
}