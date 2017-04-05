package de.fhg.iais.roberta.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.testutil.Helper;

public class MathRoundTest {
    @Test
    public void Test() throws Exception {
        String a =
            "floatElement=BlocklyMethods.round(0);floatElement2=BlocklyMethods.floor(0);floatElement3=BlocklyMethods.ceil(0);publicvoidrun()throwsException{}";

        Helper.assertCodeIsOk(a, "/syntax/math/math_round.xml");
    }
}
