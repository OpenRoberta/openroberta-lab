package de.fhg.iais.roberta.ast.syntax.stmt;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.nxt.HelperNxtForXmlTest;

public class IfStmtTest {
    private final HelperNxtForXmlTest h = new HelperNxtForXmlTest();

    @Test
    public void ifStmt() throws Exception {
        final String a =
            "\nif ( true ) {\n"
                + "}\n"
                + "if ( false ) {\n"
                + "}\n"
                + "if ( true ) {\n"
                + "    if ( false ) {\n"
                + "    }\n"
                + "}\n"
                + "if ( false ) {\n"
                + "    item = 6 + 8;\n"
                + "    item = 6 + 8;\n"
                + "} else {\n"
                + "    item = 3 * 9;\n"
                + "}\n"
                + "if ( true ) {\n"
                + "    item = 6 + 8;\n"
                + "    item = 6 + 8;\n"
                + "}\n"
                + "if ( false ) {\n"
                + "    item = 6 + 8;\n"
                + "    item = 6 + 8;\n"
                + "    item = 3 * 9;\n"
                + "} else if ( true ) {\n"
                + "    item = 3 * 9;\n"
                + "    item = 3 * 9;\n"
                + "} else {\n"
                + "    item = 3 * 9;\n"
                + "}";

        this.h.assertCodeIsOk(a, "/syntax/stmt/if_stmt.xml");
    }

    @Test
    public void ifStmt1() throws Exception {
        final String a = "\nif ( ( (5 + 7)== (5 + 7) ) >= (((5 + 7) ==( 5 + 7)) &&(( 5 + 7) <= (5 + 7)))) {\n}";

        this.h.assertCodeIsOk(a, "/syntax/stmt/if_stmt1.xml");
    }

    //
    public void ifStmt2() throws Exception {
        final String a =
            "\nif ( true ) {\n"
                + "    ;\n"
                + "    ;\n"
                + "} else if ( false ) {\n"
                + "    ;\n"
                + "} else {\n"
                + "   ;\n"
                + "}\n"
                + "if ( true ) {\n"
                + "    ;\n"
                + "} else {\n"
                + "    ;\n"
                + "    ;\n"
                + "}";

        this.h.assertCodeIsOk(a, "/syntax/stmt/if_stmt2.xml");
    }

    //
    public void ifStmt3() throws Exception {
        final String a =
            "\nif ( true ) {\n"
                + "    if ( false ) {\n"
                + "    }\n"
                + "}\n"
                + "if ( false ) {\n"
                + "    item = 6 + 8;\n"
                + "    item = 6 + 8;\n"
                + "} else {\n"
                + "    item = 3 * 9;\n"
                + "}";

        this.h.assertCodeIsOk(a, "/syntax/stmt/if_stmt3.xml");
    }

    @Test
    public void reverseTransformation() throws Exception {
        this.h.assertTransformationIsOk("/syntax/stmt/if_stmt.xml");
    }

    @Test
    public void reverseTransformation1() throws Exception {
        this.h.assertTransformationIsOk("/syntax/stmt/if_stmt1.xml");
    }

    @Test
    public void reverseTransformation2() throws Exception {
        this.h.assertTransformationIsOk("/syntax/stmt/if_stmt2.xml");
    }

    @Test
    public void reverseTransformation3() throws Exception {
        this.h.assertTransformationIsOk("/syntax/stmt/if_stmt3.xml");
    }
}