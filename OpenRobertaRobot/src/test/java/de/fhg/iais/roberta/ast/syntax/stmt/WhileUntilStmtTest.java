package de.fhg.iais.roberta.ast.syntax.stmt;

import org.junit.Test;

import de.fhg.iais.roberta.testutil.Helper;

public class WhileUntilStmtTest {

    @Test
    public void whileUntilStmt() throws Exception {
        String a =
            "\nbooleanTRUE=true;if ( TRUE ) {\nwhile ( true ) {\n"
                + "}}\n"
                + "booleanTRUE=true;if ( TRUE ) {\nwhile ( !(0 == 0) ) {\n"
                + "}}\n"
                + "booleanTRUE=true;if ( TRUE ) {\nwhile ( !true ) {\n"
                + "}}\n"
                + "booleanTRUE=true;if ( TRUE ) {\nwhile ( !(15 == 20) ) {\n"
                + "    variablenName += 1;;\n"
                + "}}\n"
                + "booleanTRUE=true;if ( TRUE ) {\nwhile ( !true ) {\n"
                + "    booleanTRUE=true;if ( TRUE ) {\nwhile ( !(15 == 20) ) {\n"
                + "        variablenName += 1;;\n"
                + "    }}\n"
                + "}}";

        Helper.assertCodeIsOk(a, "/syntax/stmt/whileUntil_stmt.xml");
    }

    @Test
    public void loopForever() throws Exception {
        String a = "booleanTRUE=true;" //
            + "if ( TRUE ) {\n"
            + "\nwhile ( true ) {\n"
            + "    System.out.println(Pickcolor.GREEN);\n"
            + "}}\n"
            + "booleanTRUE=true;if ( TRUE ) {\n"
            + "while ( true ) {\n"
            + "    System.out.println(\"\");\n"
            + "}}";

        Helper.assertCodeIsOk(a, "/ast/control/repeat_stmt_loopForever.xml");
    }

    @Test
    public void reverseTransformationWhileUntil() throws Exception {
        Helper.assertTransformationIsOk("/syntax/stmt/whileUntil_stmt.xml");
    }
}