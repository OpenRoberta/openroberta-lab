package de.fhg.iais.roberta.ast.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.testutil.Helper;

public class MathSingleTest {
    @Test
    public void Test() throws Exception {
        String a = "BlocklyMethods.sqrt(0)BlocklyMethods.abs(0)-0BlocklyMethods.log(0)BlocklyMethods.log10(0)BlocklyMethods.exp(0)BlocklyMethods.pow(10,0)";

        Helper.assertCodeIsOk(a, "/syntax/math/math_single.xml");
    }

    @Test
    public void Test1() throws Exception {
        String a = "hal.setVolume(BlocklyMethods.sqrt(0));";

        Helper.assertCodeIsOk(a, "/syntax/math/math_single1.xml");
    }

    @Test
    public void Test2() throws Exception {
        String a = "item=BlocklyMethods.sqrt(0);";

        Helper.assertCodeIsOk(a, "/syntax/math/math_single2.xml");
    }
}
