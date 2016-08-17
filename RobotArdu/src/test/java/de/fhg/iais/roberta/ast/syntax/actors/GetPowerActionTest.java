package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.testutil.Helper;

public class GetPowerActionTest {
    @Test
    public void getSpeed() throws Exception {
        final String a = "\nspeedB";

        Helper.assertCodeIsOk(a, "/ast/actions/action_MotorGetPower.xml");
    }
}
