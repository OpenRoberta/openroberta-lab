package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Ignore;

import de.fhg.iais.roberta.util.RobertaProperties;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.util.test.nxt.HelperNxtForTest;

public class LightSensorActionTest {
    HelperNxtForTest h = new HelperNxtForTest(new RobertaProperties(Util1.loadProperties(null)));

    @Ignore
    public void redOn() throws Exception {
        String a = "";

        this.h.assertCodeIsOk(a, "/ast/actions/action_LightSensorAction.xml");
    }
}