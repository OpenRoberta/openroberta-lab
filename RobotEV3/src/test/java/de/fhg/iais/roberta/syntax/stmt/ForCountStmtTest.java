package de.fhg.iais.roberta.syntax.stmt;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.ev3.Helper;

public class ForCountStmtTest {
    Helper h = new Helper();

    @Test
    public void forCountStmt() throws Exception {
        String a = "\nfor ( float i = 1; i < 10; i += 15 ) {\n" + "}\n" + "for ( float i = 1; i < 10; i += 15 ) {\n" + "    System.out.println(\"\");\n" + "}}";

        this.h.assertCodeIsOk(a, "/syntax/stmt/forCount_stmt.xml");
    }
}