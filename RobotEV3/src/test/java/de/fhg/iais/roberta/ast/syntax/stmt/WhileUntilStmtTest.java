package de.fhg.iais.roberta.ast.syntax.stmt;

import org.junit.Test;

import de.fhg.iais.roberta.testutil.ev3.Helper;

public class WhileUntilStmtTest {

    @Test
    public void whileUntilStmt() throws Exception {
        String a =
            "if ( TRUE ) {\nwhile ( true ) {\n"
                + "}}\n"
                + "if ( TRUE ) {\nwhile ( !(0 == 0) ) {\n"
                + "}}\n"
                + "if ( TRUE ) {\nwhile ( !true ) {\n"
                + "}}\n"
                + "if ( TRUE ) {\nwhile ( !(15 == 20) ) {\n"
                + "    variablenName += 1;\n"
                + "}}\n"
                + "if ( TRUE ) {\nwhile ( !true ) {\n"
                + "    if ( TRUE ) {\nwhile ( !(15 == 20) ) {\n"
                + "        variablenName += 1;\n"
                + "    }}\n"
                + "}}";

        Helper.assertCodeIsOk(a, "/syntax/stmt/whileUntil_stmt.xml");
    }

    @Test
    public void loopForever() throws Exception {
        String a = //
            "if ( TRUE ) {\n"
                + "\nwhile ( true ) {\n"
                + "    System.out.println(Pickcolor.GREEN);\n"
                + "}}\n"
                + "if ( TRUE ) {\n"
                + "while ( true ) {\n"
                + "    System.out.println(\"\");\n"
                + "}}";

        Helper.assertCodeIsOk(a, "/syntax/control/repeat_stmt_loopForever.xml");
    }

    @Test
    public void reverseTransformationWhileUntil() throws Exception {
        Helper.assertTransformationIsOk("/syntax/stmt/whileUntil_stmt.xml");
    }
}