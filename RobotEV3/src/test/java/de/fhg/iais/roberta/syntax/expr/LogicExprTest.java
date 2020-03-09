package de.fhg.iais.roberta.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.Ev3LejosAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class LogicExprTest extends Ev3LejosAstTest {

    @Test
    public void test1() throws Exception {
        String a =
            "\nfalse == true\n"
                + "true != false\n"
                + "false == false\n"
                + "(5 <= 7) == (8 > 9)\n"
                + "( 5 != 7 ) >= ( 8 == 9 )\n"
                + "(5 + 7) >= (( 8 + 4 ) / ((float) ( 9 + 3 )))\n"
                + "( (5 + 7) == (5 + 7) ) >= (( 8 + 4 ) / ((float) ( 9 + 3 )))\n"
                + "( (5 + 7) == (5 + 7) ) >= ( ((5 + 7) == (5 + 7)) && ((5 + 7) <= (5 + 7) ))\n"
                + "!((5 + 7) == (5 + 7)) == true}";
        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/expr/logic_expr.xml", false);
    }

    @Test
    public void logicNegate() throws Exception {
        String a = "\n!((0 != 0) && false)}";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/expr/logic_negate.xml", false);
    }

    @Test
    public void logicNull() throws Exception {
        String a = "\nnull}";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/expr/logic_null.xml", false);
    }

    @Test
    public void logicTernary() throws Exception {
        String a = "\n( ( 0 == 0 ) ? false : true )}";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/expr/logic_ternary.xml", false);
    }
}
