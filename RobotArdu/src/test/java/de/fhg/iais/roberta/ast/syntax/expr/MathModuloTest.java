package de.fhg.iais.roberta.ast.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.ardu.HelperBotNroll;

public class MathModuloTest {
    HelperBotNroll h = new HelperBotNroll();

    @Test
    public void Test() throws Exception {
        String a = "doublevariablenName=1%0;voidloop(){";

        this.h.assertCodeIsOk(a, "/syntax/math/math_modulo.xml", false);
    }

}
