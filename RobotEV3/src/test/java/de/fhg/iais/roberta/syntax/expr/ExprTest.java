package de.fhg.iais.roberta.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.Ev3LejosAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class ExprTest extends Ev3LejosAstTest {

    @Test
    public void test1() throws Exception {
        String a =
            "\n8 + (-3 + 5)\n" + "88 - ( 8 + (-3 + 5) )\n" + "(88 - ( 8 + (-3 + 5) )) - ( 88 - ( 8 + (-3 + 5) ) )\n" + "2 * ( 2 - 2 )\n" + "2 - (2 * 2)}";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/expr/expr1.xml", false);
    }

    @Test
    public void test2() throws Exception {
        String a =
            "\n2 * ( 2 - 2 )\n"
                + "2 - (2 * 2)\n"
                + "(88 - ( 8 + (-3 + 5) )) - (2 * 2)\n"
                + "((88 - ( 8 + (-3 + 5) )) - (2 * 2)) / ((float) (( 88 - ( 8 + (-3 + 5) )) - (2 * 2) ))}";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/expr/expr2.xml", false);
    }
}