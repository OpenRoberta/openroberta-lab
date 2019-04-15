package de.fhg.iais.roberta.ast.syntax.stmt;

import de.fhg.iais.roberta.util.test.nxt.HelperNxtForXmlTest;

public class FlowControlStmtTest {
    private final HelperNxtForXmlTest h = new HelperNxtForXmlTest();

    //
    public void flowControlStmt() throws Exception {
        String a =
            "\nwhile ( 0 == 0 ) \n"

                + "    \nwhile ( !(0 == 0) ) \n"

                + "       break;\n"
                + "    }\n"
                + "    break;\n"
                + "}";

        this.h.assertCodeIsOk(a, "/syntax/stmt/flowControl_stmt.xml");
    }
}