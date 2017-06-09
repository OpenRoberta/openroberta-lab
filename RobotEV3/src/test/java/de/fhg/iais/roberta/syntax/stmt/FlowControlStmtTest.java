package de.fhg.iais.roberta.syntax.stmt;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.ev3.Helper;

public class FlowControlStmtTest {
    Helper h = new Helper();

    @Test
    public void flowControlStmt() throws Exception {
        String a =
            "while ( 0 == 0 ) {\n"
                + "    System.out.println(\"123\");\n"
                + "    System.out.println(\"123\");\n"
                + "    while ( !(0 == 0) ) {\n"
                + "        System.out.println(\"123\");\n"
                + "        System.out.println(\"123\");\n"
                + "        break;\n"
                + "    }\n"
                + "    break;\n"
                + "}}";

        this.h.assertCodeIsOk(a, "/syntax/stmt/flowControl_stmt.xml");
    }
}