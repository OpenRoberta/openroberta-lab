package de.fhg.iais.roberta.ast.syntax.stmt;

import org.junit.Ignore;

import de.fhg.iais.roberta.ast.syntax.codeGeneration.Helper;

public class WaitStmtTest {

    @Ignore
    public void test1() throws Exception {
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

        Helper.assertCodeIsOk(a, "/ast/control/wait_stmt2.xml");
    }
}