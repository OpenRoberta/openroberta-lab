package de.fhg.iais.roberta.syntax.stmt;

import org.junit.Test;

import de.fhg.iais.roberta.testutil.Helper;

public class FlowControlStmtTest {

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

        Helper.assertCodeIsOk(a, "/syntax/stmt/flowControl_stmt.xml");
    }
}