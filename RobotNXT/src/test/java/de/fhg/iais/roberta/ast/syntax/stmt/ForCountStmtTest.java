package de.fhg.iais.roberta.ast.syntax.stmt;

import de.fhg.iais.roberta.util.test.nxt.Helper;

public class ForCountStmtTest {
    Helper h = new Helper();

    //
    public void forCountStmt() throws Exception {
        String a = "\nfor ( float i = 1; i < 10; i += 15 ) {\n" + "}\n" + "for ( float i = 1; i < 10; i += 15 ) {\n" + "    ;\n" + "}";

        this.h.assertCodeIsOk(a, "/syntax/stmt/forCount_stmt.xml");
    }
}