package de.fhg.iais.roberta.syntax.stmt;

import org.junit.Ignore;
import org.junit.Test;

import de.fhg.iais.roberta.Ev3LejosAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class WhileUntilStmtTest extends Ev3LejosAstTest {

    // TODO Invalid test
    @Ignore
    @Test
    public void whileUntilStmt() throws Exception {
        String a =
            "while ( true ) {\n"
                + "}\n"
                + "while ( !(0 == 0) ) {\n"
                + "}\n"
                + "while ( !true ) {\n"
                + "}\n"
                + "while ( !(15 == 20) ) {\n"
                + "    variablenName += 1;\n"
                + "}\n"
                + "while ( !true ) {\n"
                + "    while ( !(15 == 20) ) {\n"
                + "        variablenName += 1;\n"
                + "    }\n"
                + "}}";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/stmt/whileUntil_stmt.xml", false);
    }

    @Test
    public void loopForever() throws Exception {
        String a = //
            "if ( true ) {\n"
                + "\nwhile ( true ) {\n"
                + "    System.out.println(PickColor.GREEN);\n"
                + "}}\n"
                + "if ( true ) {\n"
                + "while ( true ) {\n"
                + "    System.out.println(\"\");\n"
                + "}}}";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/control/repeat_stmt_loopForever.xml", false);
    }

    @Test
    public void reverseTransformationWhileUntil() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/syntax/stmt/whileUntil_stmt.xml");
    }
}