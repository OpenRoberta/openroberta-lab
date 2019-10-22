package de.fhg.iais.roberta.ast.syntax.stmt;

import de.fhg.iais.roberta.NxtAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class FlowControlStmtTest extends NxtAstTest {

    //
    public void flowControlStmt() throws Exception {
        String a =
            "\nwhile ( 0 == 0 ) \n"

                + "    \nwhile ( !(0 == 0) ) \n"

                + "       break;\n"
                + "    }\n"
                + "    break;\n"
                + "}";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/stmt/flowControl_stmt.xml", false);
    }
}