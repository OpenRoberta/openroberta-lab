package de.fhg.iais.roberta.ast.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.codegen.lejos.Helper;

public class MathNumberPropertyTest {
    @Test
    public void Test() throws Exception {
        String a =
            "BlocklyMethods.isEven(0)BlocklyMethods.isOdd(0)BlocklyMethods.isPrime(0)BlocklyMethods.isWhole(0)BlocklyMethods.isPositive(0)BlocklyMethods.isNegative(0)BlocklyMethods.isDivisibleBy(0,0)";

        Helper.assertCodeIsOk(a, "/syntax/math/math_number_property.xml");
    }

    @Test
    public void Test1() throws Exception {
        String a = "item=BlocklyMethods.isEven(0);";

        Helper.assertCodeIsOk(a, "/syntax/math/math_number_property1.xml");
    }

}
