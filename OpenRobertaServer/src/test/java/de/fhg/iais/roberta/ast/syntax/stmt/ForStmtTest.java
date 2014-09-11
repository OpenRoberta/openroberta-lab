package de.fhg.iais.roberta.ast.syntax.stmt;

import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.codeGeneration.Helper;

public class ForStmtTest {

    @Test
    public void forStmt() throws Exception {
        String a =
            "\nfor ( int i = 0; i < 10; i++ ) {\n"
                + "}\n"
                + "for ( int i = 0; i < 10; i++ ) {\n"
                + "    System.out.println(\"15\");\n"
                + "    System.out.println(\"15\");\n"
                + "}\n"
                + "for ( int i = 0; i < 10; i++ ) {\n"
                + "    for ( int i = 0; i < 10; i++ ) {\n"
                + "        System.out.println(\"15\");\n"
                + "        System.out.println(\"15\");\n"
                + "    }\n"
                + "}";

        Helper.assertCodeIsOk(a, "/syntax/stmt/for_stmt.xml");
    }
}