package de.fhg.iais.roberta.ast.syntax.stmt;

import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.codeGeneration.Helper;

public class ForStmtTest {

    @Test
    public void forStmt() throws Exception {
        String a =
            "\nfor ( int i0 = 0; i0 < 10; i0++ ) {\n"
                + "}\n"
                + "for ( int i1 = 0; i1 < 10; i1++ ) {\n"
                + "    System.out.println(\"15\");\n"
                + "    System.out.println(\"15\");\n"
                + "}\n"
                + "for ( int i2 = 0; i2 < 10; i2++ ) {\n"
                + "    for ( int i3 = 0; i3 < 10; i3++ ) {\n"
                + "        System.out.println(\"15\");\n"
                + "        System.out.println(\"15\");\n"
                + "    }\n"
                + "}";

        Helper.assertCodeIsOk(a, "/syntax/stmt/for_stmt.xml");
    }

    @Test
    public void forStmt1() throws Exception {
        String a =
            "for ( int i0 = 0; i0 < 10; i0++ ) {item3 + String.valueOf(\"Proba\");item3 + String.valueOf(\"Proba1\");for ( int i1 = 0; i1 < 10; i1++ ) {}}";

        Helper.assertCodeIsOk(a, "/ast/control/repeat_stmt.xml");
    }

    @Test
    public void reverseTransformation() throws Exception {
        Helper.assertTransformationIsOk("/syntax/stmt/for_stmt.xml");
    }
}