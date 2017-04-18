package de.fhg.iais.roberta.ast.syntax.expr;

import org.junit.Ignore;
import org.junit.Test;

import de.fhg.iais.roberta.testutil.Helper;

public class LogicExprTest {

    @Test
    public void test1() throws Exception {
        final String a =
            "\nfalse == true\n"
                + "true != false\n"
                + "false == false\n"
                + "(5 <= 7)==(8 > 9)\n"
                + " (5 != 7)>=(8 == 9) \n"
<<<<<<< ce5e36408b7f6f9cacc93041b87e296db1504c33
                + "(5 + 7)>= ((8 + 4)/ (9 + 3)) \n"
                + "((5 + 7)==(5 + 7 ))>= ((8 + 4 ) / ( 9 + 3)) \n"
=======
                + "(5 + 7)>= ((8 + 4)/  (9 + 3)) \n"
                + "((5 + 7)==(5 + 7 ))>= ((8 + 4) / (9 + 3)) \n"
>>>>>>> #474 remove casting to float NXC
                + "((5 + 7)==(5 + 7) )>= (((5 + 7)== (5 + 7)) && ((5 + 7) <= (5 + 7)))\n"
                + "!((5 + 7)==(5 + 7) )== true";

        Helper.assertCodeIsOk(a, "/syntax/expr/logic_expr.xml");
    }

    @Test
    public void logicNegate() throws Exception {
        final String a = "\n!((0!= 0)&&false)";

        Helper.assertCodeIsOk(a, "/syntax/expr/logic_negate.xml");
    }

    @Test
    public void logicNull() throws Exception {
        final String a = "\nNULL";

        Helper.assertCodeIsOk(a, "/syntax/expr/logic_null.xml");
    }

    // The ternary was removed
    @Ignore
    public void logicTernary() throws Exception {
        final String a = "\n( 0 == 0 ) ? false : true";

        Helper.assertCodeIsOk(a, "/syntax/expr/logic_ternary.xml");
    }
}