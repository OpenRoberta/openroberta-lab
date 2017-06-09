package de.fhg.iais.roberta.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.ev3.Helper;

public class MathRandomIntTest {
    Helper h = new Helper();

    @Test
    public void Test() throws Exception {
        String a = "BlocklyMethods.randInt(1,100)}";

        this.h.assertCodeIsOk(a, "/syntax/math/math_random_int.xml");
    }
}
