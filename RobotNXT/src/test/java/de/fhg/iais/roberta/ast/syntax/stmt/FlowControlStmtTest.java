package de.fhg.iais.roberta.ast.syntax.stmt;

import de.fhg.iais.roberta.testutil.Helper;

public class FlowControlStmtTest {

    //
    public void flowControlStmt() throws Exception {
        String a = "\nwhile ( 0 == 0 ) \n"

            + "    \nwhile ( !(0 == 0) ) \n"

            + "       break;\n"
            + "    }\n"
            + "    break;\n"
            + "}";

        Helper.assertCodeIsOk(a, "/syntax/stmt/flowControl_stmt.xml");
    }
}