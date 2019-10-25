package de.fhg.iais.roberta.syntax.stmt;

import org.junit.Test;

import de.fhg.iais.roberta.Ev3LejosAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class ForCountStmtTest extends Ev3LejosAstTest {

    @Test
    public void forCountStmt() throws Exception {
        String a =
            "\nfor ( float ___i = 1; ___i < 10; ___i += 15 ) {\n"
                + "}\n"
                + "for ( float ___i = 1; ___i < 10; ___i += 15 ) {\n"
                + "    System.out.println(\"\");\n"
                + "}}";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/stmt/forCount_stmt.xml", false);
    }
}