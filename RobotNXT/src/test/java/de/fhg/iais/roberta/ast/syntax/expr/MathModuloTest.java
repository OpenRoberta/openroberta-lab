package de.fhg.iais.roberta.ast.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.nxt.Helper;

public class MathModuloTest {
    Helper h = new Helper();

    @Test
    public void Test() throws Exception {
        String a = "floatvariablenName=1%0;taskmain(){";

        this.h.assertCodeIsOk(a, "/syntax/math/math_modulo.xml");
    }

}
