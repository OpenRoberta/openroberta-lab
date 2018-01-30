package de.fhg.iais.roberta.ast.syntax.stmt;

import de.fhg.iais.roberta.util.RobertaProperties;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.util.test.nxt.HelperNxtForTest;

public class FlowControlStmtTest {
    HelperNxtForTest h = new HelperNxtForTest(new RobertaProperties(Util1.loadProperties(null)));

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