package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Ignore;

import de.fhg.iais.roberta.util.test.nxt.HelperNxtForXmlTest;

public class LightSensorActionTest {
    private final HelperNxtForXmlTest h = new HelperNxtForXmlTest();

    @Ignore
    public void redOn() throws Exception {
        String a = "";

        this.h.assertCodeIsOk(a, "/ast/actions/action_LightSensorAction.xml");
    }
}