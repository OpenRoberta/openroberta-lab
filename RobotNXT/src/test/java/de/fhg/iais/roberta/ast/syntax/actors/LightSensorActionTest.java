package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Ignore;

import de.fhg.iais.roberta.util.test.nxt.Helper;

public class LightSensorActionTest {
    Helper h = new Helper();

    @Ignore
    public void redOn() throws Exception {
        String a = "";

        this.h.assertCodeIsOk(a, "/ast/actions/action_LightSensorAction.xml");
    }
}