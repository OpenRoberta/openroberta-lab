package de.fhg.iais.roberta.ast.syntax.stmt;

import org.junit.Ignore;
import org.junit.Test;

import de.fhg.iais.roberta.NxtAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class WhileUntilStmtTest extends NxtAstTest {

    // TODO invalid test
    @Ignore
    @Test
    public void whileUntilStmt() throws Exception {
        String a =
            "\nwhile ( true ) {\n"
                + "}\n"
                + "\nwhile ( !(0 == 0) ) {\n"
                + "}\n"
                + "\nwhile ( !true ) {\n"
                + "}\n"
                + "\nwhile ( !(15 == 20) ) {\n"
                + "    ___variablenName += 1;\n"
                + "}\n"
                + "\nwhile ( !true ) {\n"
                + "    \nwhile ( !(15 == 20) ) {\n"
                + "        ___variablenName += 1;\n"
                + "    }\n"
                + "}";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/stmt/whileUntil_stmt.xml", false);
    }

    //
    public void loopForever() throws Exception {
        String a = //

            "while ( true ) {\n" + "    ;\n" + "}" + "\n" + "while ( true ) {\n" + "    ;\n" + "}";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/ast/control/repeat_stmt_loopForever.xml", false);
    }

    @Test
    public void reverseTransformationWhileUntil() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/syntax/stmt/whileUntil_stmt.xml");
    }
}