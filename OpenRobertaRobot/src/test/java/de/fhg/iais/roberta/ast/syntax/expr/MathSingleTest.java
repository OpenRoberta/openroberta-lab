package de.fhg.iais.roberta.ast.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.testutil.Helper;

public class MathSingleTest {
    @Test
    public void Test() throws Exception {
        String a = "((float)Math.sqrt(0))((float)Math.abs(0))-0((float)Math.log(0))((float)Math.log10(0))((float)Math.exp(0))((float)Math.pow(10,0))";

        Helper.assertCodeIsOk(a, "/syntax/math/math_single.xml");
    }

    @Test
    public void Test1() throws Exception {
        String a = "hal.setVolume(((float)Math.sqrt(0)));";

        Helper.assertCodeIsOk(a, "/syntax/math/math_single1.xml");
    }

    @Test
    public void Test2() throws Exception {
        String a = "item=((float)Math.sqrt(0));";

        Helper.assertCodeIsOk(a, "/syntax/math/math_single2.xml");
    }
}
