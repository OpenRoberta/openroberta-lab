package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.util.RobertaProperties;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.util.test.nxt.HelperNxtForXmlTest;

public class MotorDriveStopActionTest {
    private final HelperNxtForXmlTest h = new HelperNxtForXmlTest();

    @Test
    public void stop() throws Exception {
        final String a = "\nOff(OUT_BC);";

        this.h.assertCodeIsOk(a, "/ast/actions/action_Stop.xml");
    }
}