package de.fhg.iais.roberta.ast.syntax.expr;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.helper.Helper;

public class LogicExprTest {

    @Test
    public void test1() throws Exception {
        String a =
            "\nfalse == true\n"
                + "true != false\n"
                + "false == false\n"
                + "5 <= 7 == 8 > 9\n"
                + "( 5 != 7 ) >= ( 8 == 9 )\n"
                + "5 + 7 >= ( 8 + 4 ) / ( 9 + 3 )\n"
                + "( 5 + 7 == 5 + 7 ) >= ( 8 + 4 ) / ( 9 + 3 )\n"
                + "( 5 + 7 == 5 + 7 ) >= ( 5 + 7 == 5 + 7 && 5 + 7 <= 5 + 7 )\n"
                + "!(5 + 7 == 5 + 7) == true";

        Assert.assertEquals(a, Helper.generateSyntax("/syntax/expr/logic_expr.xml"));
    }

    @Test
    public void logicNegate() throws Exception {
        String a = "\n!(0 != 0 && false)";

        Assert.assertEquals(a, Helper.generateSyntax("/syntax/expr/logic_negate.xml"));
    }

    @Test
    public void logicNull() throws Exception {
        String a = "\nnull";

        Assert.assertEquals(a, Helper.generateSyntax("/syntax/expr/logic_null.xml"));
    }

    @Test
    public void logicTernary() throws Exception {
        String a = "\n( 0 == 0 ) ? false : true";

        Assert.assertEquals(a, Helper.generateSyntax("/syntax/expr/logic_ternary.xml"));
    }
}