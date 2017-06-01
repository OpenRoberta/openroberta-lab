package de.fhg.iais.roberta.ast.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.nxt.Helper;

public class MathRoundTest {
    Helper h = new Helper();

    @Test
    public void Test() throws Exception {
        final String a = "MathRound(0)MathRoundUp(0)MathFloor(0)";

        this.h.assertCodeIsOk(a, "/syntax/math/math_round.xml");
    }
}
