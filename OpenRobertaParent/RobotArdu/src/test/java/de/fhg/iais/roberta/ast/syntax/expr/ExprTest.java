package de.fhg.iais.roberta.ast.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.util.RobertaProperties;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.util.test.ardu.HelperBotNrollForTest;

public class ExprTest {
    HelperBotNrollForTest h = new HelperBotNrollForTest(new RobertaProperties(Util1.loadProperties(null)));

    @Test
    public void test1() throws Exception {
        final String a = "\n8 + (-3 + 5)88 - ( 8 + (-3 + 5))(88 - ( 8 + (-3 + 5)))  - ( 88 - ( 8 + (-3 + 5) ))2 * ( 2 - 2 )\n" + "2 - (2 * 2)";

        this.h.assertCodeIsOk(a, "/syntax/expr/expr1.xml", false);
    }

    @Test
    public void test2() throws Exception {
        final String a =
            "\n2 * ( 2 - 2 )\n" + "2 - (2 * 2)(88 - ( 8 + (-3 + 5))) - (2 * 2)((88 - ( 8 + (-3 + 5))) - (2 * 2) )/((float) ((88 -( 8 + (-3 + 5)))-(2 * 2) ))";

        this.h.assertCodeIsOk(a, "/syntax/expr/expr2.xml", false);
    }
}