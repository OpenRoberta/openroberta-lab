package de.fhg.iais.roberta.ast.syntax.stmt;

import de.fhg.iais.roberta.NxtAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class ForCountStmtTest extends NxtAstTest {

    //
    public void forCountStmt() throws Exception {
        String a = "\nfor ( float i = 1; i < 10; i += 15 ) {\n" + "}\n" + "for ( float i = 1; i < 10; i += 15 ) {\n" + "    ;\n" + "}";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/stmt/forCount_stmt.xml", false);
    }
}