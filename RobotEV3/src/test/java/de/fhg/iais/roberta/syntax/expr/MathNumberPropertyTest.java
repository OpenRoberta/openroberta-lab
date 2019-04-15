package de.fhg.iais.roberta.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.ev3.HelperEv3ForXmlTest;

public class MathNumberPropertyTest {
    private final HelperEv3ForXmlTest h = new HelperEv3ForXmlTest();

    @Test
    public void Test() throws Exception {
        String a =
            "booleanElement=BlocklyMethods.isPrime(0);booleanElement2=BlocklyMethods.isWhole(0);booleanElement3=BlocklyMethods.isEven(0);booleanElement4=BlocklyMethods.isOdd(0);booleanElement5=BlocklyMethods.isPositive(0);booleanElement6=BlocklyMethods.isDivisibleBy(0,0);booleanElement7=BlocklyMethods.isNegative(0);publicvoidrun()throwsException{}";

        this.h.assertCodeIsOk(a, "/syntax/math/math_number_property.xml");
    }
}
