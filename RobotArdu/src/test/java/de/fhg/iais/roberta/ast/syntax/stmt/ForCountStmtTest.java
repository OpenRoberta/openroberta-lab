package de.fhg.iais.roberta.ast.syntax.stmt;

import de.fhg.iais.roberta.testutil.Helper;

public class ForCountStmtTest {
    //
    public void forCountStmt() throws Exception {
        String a = "\nfor ( float i = 1; i < 10; i += 15 ) {\n" + "}\n" + "for ( float i = 1; i < 10; i += 15 ) {\n" + "    ;\n" + "}";

        Helper.assertCodeIsOk(a, "/syntax/stmt/forCount_stmt.xml");
    }
}