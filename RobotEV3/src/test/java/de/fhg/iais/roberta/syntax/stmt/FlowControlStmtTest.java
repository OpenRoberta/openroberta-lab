package de.fhg.iais.roberta.syntax.stmt;

import org.junit.Test;

import de.fhg.iais.roberta.Ev3LejosAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class FlowControlStmtTest extends Ev3LejosAstTest {

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

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/stmt/flowControl_stmt.xml", false);
    }
}