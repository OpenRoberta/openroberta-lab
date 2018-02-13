package de.fhg.iais.roberta.syntax.stmt;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.ev3.HelperEv3ForXmlTest;

public class WhileUntilStmtTest {
    private final HelperEv3ForXmlTest h = new HelperEv3ForXmlTest();

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

        this.h.assertCodeIsOk(a, "/syntax/stmt/whileUntil_stmt.xml");
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

        this.h.assertCodeIsOk(a, "/syntax/control/repeat_stmt_loopForever.xml");
    }

    @Test
    public void reverseTransformationWhileUntil() throws Exception {
        this.h.assertTransformationIsOk("/syntax/stmt/whileUntil_stmt.xml");
    }
}