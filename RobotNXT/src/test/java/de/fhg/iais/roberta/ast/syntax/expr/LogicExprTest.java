package de.fhg.iais.roberta.ast.syntax.expr;

import org.junit.Ignore;
import org.junit.Test;

import de.fhg.iais.roberta.NxtAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class LogicExprTest extends NxtAstTest {

    @Test
    public void test1() throws Exception {
        final String a =
            "\nfalse == true\n"
                + "true != false\n"
                + "false == false\n"
                + "(5 <= 7)==(8 > 9)\n"
                + " (5 != 7)>=(8 == 9) \n"
                + "( (5 + 7) ) >= ( ( ( (8 + 4) ) / ( ( ( (9 + 3) ) )*1.0)  )   ) \n"
                + "( ( (5 + 7) ) == (( 5 + 7 )) ) >= (  (( ( 8 + 4 )) / (  (( ( 9 + 3)) )*1.0)  )   ) \n"
                + "(( (5 + 7) ) ==((5 + 7) )) >= (  (( (5 + 7)) == ((5 + 7)) ) && ( ((5 + 7)) <= ((5 + 7)) )  )\n"
                + "!(((5 + 7))==((5 + 7)) )== true";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/expr/logic_expr.xml", false);
    }

    @Test
    public void logicNegate() throws Exception {
        final String a = "\n!((0!= 0)&&false)";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/expr/logic_negate.xml", false);
    }

    @Test
    public void logicNull() throws Exception {
        final String a = "\nNULL";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/expr/logic_null.xml", false);
    }

    // The ternary was removed
    @Ignore
    public void logicTernary() throws Exception {
        final String a = "\n( 0 == 0 ) ? false : true";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/expr/logic_ternary.xml", false);
    }
}