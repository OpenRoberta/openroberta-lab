package de.fhg.iais.roberta.ast.syntax.stmt;

import org.junit.Test;

import de.fhg.iais.roberta.testutil.Helper;

public class WhileUntilStmtTest {

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
                + "    variablenName += 1;\n"
                + "}\n"
                + "\nwhile ( !true ) {\n"
                + "    \nwhile ( !(15 == 20) ) {\n"
                + "        variablenName += 1;\n"
                + "    }\n"
                + "}";

        Helper.assertCodeIsOk(a, "/syntax/stmt/whileUntil_stmt.xml");
    }

    //
    public void loopForever() throws Exception {
        String a = //

            "while ( true ) {\n" + "    ;\n" + "}" + "\n" + "while ( true ) {\n" + "    ;\n" + "}";

        Helper.assertCodeIsOk(a, "/ast/control/repeat_stmt_loopForever.xml");
    }

    @Test
    public void reverseTransformationWhileUntil() throws Exception {
        Helper.assertTransformationIsOk("/syntax/stmt/whileUntil_stmt.xml");
    }
}