package de.fhg.iais.roberta.ast.syntax.stmt;

import de.fhg.iais.roberta.util.test.nxt.Helper;

public class FlowControlStmtTest {
    Helper h = new Helper();

    //
    public void flowControlStmt() throws Exception {
        String a = "\nwhile ( 0 == 0 ) \n"

            + "    \nwhile ( !(0 == 0) ) \n"

            + "       break;\n"
            + "    }\n"
            + "    break;\n"
            + "}";

        this.h.assertCodeIsOk(a, "/syntax/stmt/flowControl_stmt.xml");
    }
}