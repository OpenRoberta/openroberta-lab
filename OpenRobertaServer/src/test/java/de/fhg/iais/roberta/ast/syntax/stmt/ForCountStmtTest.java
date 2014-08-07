package de.fhg.iais.roberta.ast.syntax.stmt;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.helper.Helper;

public class ForCountStmtTest {

    @Test
    public void forCountStmt() throws Exception {
        String a = "\nfor ( int i = 1; i <= 10; i += 15 ) {\n" + "}\n" + "for ( int i = 1; i <= 10; i += 15 ) {\n" + "    System.out.println(\"\");\n" + "}";

        Assert.assertEquals(a, Helper.generateSyntax("/syntax/stmt/forCount_stmt.xml"));
    }
}