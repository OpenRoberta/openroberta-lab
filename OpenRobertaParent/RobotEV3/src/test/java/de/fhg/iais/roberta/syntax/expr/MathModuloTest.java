package de.fhg.iais.roberta.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.ev3.Helper;

public class MathModuloTest {
    Helper h = new Helper();

    @Test
    public void Test() throws Exception {
        String a = "floatvariablenName=1%0;publicvoidrun()throwsException{}";

        this.h.assertCodeIsOk(a, "/syntax/math/math_modulo.xml");
    }

}
