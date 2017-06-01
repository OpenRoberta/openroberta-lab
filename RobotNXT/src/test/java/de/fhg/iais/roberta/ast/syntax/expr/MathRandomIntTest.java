package de.fhg.iais.roberta.ast.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.nxt.Helper;

public class MathRandomIntTest {
    Helper h = new Helper();

    @Test
    public void Test() throws Exception {
        final String a = "RandomIntegerInRange(1,100)";

        this.h.assertCodeIsOk(a, "/syntax/math/math_random_int.xml");
    }
}
