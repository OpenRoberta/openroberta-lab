package de.fhg.iais.roberta.ast.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.testutil.Helper;

public class MathRoundTest {
    @Test
    public void Test() throws Exception {
        String a = "BlocklyMethods.round(0)BlocklyMethods.ceil(0)BlocklyMethods.floor(0)";

        Helper.assertCodeIsOk(a, "/syntax/math/math_round.xml");
    }
}
