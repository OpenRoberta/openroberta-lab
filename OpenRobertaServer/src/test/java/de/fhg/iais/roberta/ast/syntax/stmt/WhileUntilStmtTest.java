package de.fhg.iais.roberta.ast.syntax.stmt;

import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.codeGeneration.Helper;

public class WhileUntilStmtTest {

    @Test
    public void whileUntilStmt() throws Exception {
        String a =
            "\nwhile ( true ) {\n"
                + "}\n"
                + "while ( !(0 == 0) ) {\n"
                + "}\n"
                + "while ( !true ) {\n"
                + "}\n"
                + "while ( !(15 == 20) ) {\n"
                + "    item MATH_CHANGE 1;\n"
                + "}\n"
                + "while ( !true ) {\n"
                + "    while ( !(15 == 20) ) {\n"
                + "        item MATH_CHANGE 1;\n"
                + "    }\n"
                + "}";

        Helper.assertCodeIsOk(a, "/syntax/stmt/whileUntil_stmt.xml");
    }

    @Test
    public void loopForever() throws Exception {
        String a = "" //
            + "\nwhile ( true ) {\n"
            + "    System.out.println(1);\n"
            + "}\n"
            + "while ( true ) {\n"
            + "    System.out.println(\"\");\n"
            + "}";

        Helper.assertCodeIsOk(a, "/ast/control/repeat_stmt_loopForever.xml");
    }
}