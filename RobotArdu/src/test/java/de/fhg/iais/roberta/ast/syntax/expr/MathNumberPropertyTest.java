package de.fhg.iais.roberta.ast.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.ardu.HelperBotNroll;

public class MathNumberPropertyTest {
    HelperBotNroll h = new HelperBotNroll();

    @Test
    public void Test() throws Exception {
        final String a = "(fmod(0,2)==0)(fmod(0,2)!=0)rob.isPrime(0)rob.isWhole(0)(0>0)(0<0)(fmod(0,0)==0)";

        this.h.assertCodeIsOk(a, "/syntax/math/math_number_property.xml", false);
    }

    @Test
    public void Test1() throws Exception {
        final String a = "item=(fmod(0,2)==0);";

        this.h.assertCodeIsOk(a, "/syntax/math/math_number_property1.xml", false);
    }

}
