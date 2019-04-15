package de.fhg.iais.roberta.syntax.stmt;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.ev3.HelperEv3ForXmlTest;

public class ForStmtTest {
    private final HelperEv3ForXmlTest h = new HelperEv3ForXmlTest();

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
                + "}}";

        this.h.assertCodeIsOk(a, "/syntax/stmt/for_stmt.xml");
    }

    @Test
    public void forStmt1() throws Exception {
        String a =
            "for ( float k0 = 0; k0 < 10; k0+=1 ) {item3 += String.valueOf(\"Proba\");item3 += String.valueOf(\"Proba1\");for ( float k1 = 0; k1 < 10; k1+=1 ) {}}}";

        this.h.assertCodeIsOk(a, "/syntax/control/repeat_stmt.xml");
    }

    @Test
    public void reverseTransformation() throws Exception {
        this.h.assertTransformationIsOk("/syntax/stmt/for_stmt.xml");
    }
}