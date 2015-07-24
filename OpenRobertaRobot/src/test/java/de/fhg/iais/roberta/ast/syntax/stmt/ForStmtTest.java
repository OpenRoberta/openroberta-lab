package de.fhg.iais.roberta.ast.syntax.stmt;

import org.junit.Test;

import de.fhg.iais.roberta.testutil.Helper;

public class ForStmtTest {

    @Test
    public void forStmt() throws Exception {
        String a =
            "\nfor ( float k0 = 0; k0 < 10; k0+=1 ) {\n"
                + "}\n"
                + "for ( float k1 = 0; k1 < 10; k1+=1 ) {\n"
                + "    System.out.println(\"15\");\n"
                + "    System.out.println(\"15\");\n"
                + "}\n"
                + "for ( float k2 = 0; k2 < 10; k2+=1 ) {\n"
                + "    for ( float k3 = 0; k3 < 10; k3+=1 ) {\n"
                + "        System.out.println(\"15\");\n"
                + "        System.out.println(\"15\");\n"
                + "    }\n"
                + "}";

        Helper.assertCodeIsOk(a, "/syntax/stmt/for_stmt.xml");
    }

    @Test
    public void forStmt1() throws Exception {
        String a =
            "for ( float k0 = 0; k0 < 10; k0+=1 ) {item3 + String.valueOf(\"Proba\");item3 + String.valueOf(\"Proba1\");for ( float k1 = 0; k1 < 10; k1+=1 ) {}}";

        Helper.assertCodeIsOk(a, "/ast/control/repeat_stmt.xml");
    }

    @Test
    public void reverseTransformation() throws Exception {
        Helper.assertTransformationIsOk("/syntax/stmt/for_stmt.xml");
    }
}