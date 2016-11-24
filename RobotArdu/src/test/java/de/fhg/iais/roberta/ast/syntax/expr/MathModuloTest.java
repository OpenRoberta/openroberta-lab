package de.fhg.iais.roberta.ast.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.testutil.Helper;

public class MathModuloTest {
    @Test
    public void Test() throws Exception {
        String a = "doublevariablenName=1%0;voidloop(){";

        Helper.assertCodeIsOk(a, "/syntax/math/math_modulo.xml");
    }

}
