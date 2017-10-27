package de.fhg.iais.roberta.ast.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.nxt.Helper;

public class MathConstrainTest {
    Helper h = new Helper();

    @Test
    public void Test() throws Exception {
        final String a = "Constrain(SensorUS(S4),1,100)";

        this.h.assertCodeIsOk(a, "/syntax/math/math_constrain.xml");
    }
}
