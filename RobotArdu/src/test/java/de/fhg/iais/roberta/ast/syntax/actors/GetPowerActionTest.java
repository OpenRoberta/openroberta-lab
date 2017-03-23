package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.ardu.Helper;

public class GetPowerActionTest {
    Helper h = new Helper();

    @Test
    public void getSpeed() throws Exception {
        final String a = "\n";

        this.h.assertCodeIsOk(a, "/ast/actions/action_MotorGetPower.xml", false);
    }
}
