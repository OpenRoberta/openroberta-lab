package de.fhg.iais.roberta.ast.syntax.stmt;

import org.junit.Test;

import de.fhg.iais.roberta.NxtAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class IfStmtTest extends NxtAstTest {

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
                + "    ___item = (6 + 8);\n"
                + "    ___item = (6 + 8);\n"
                + "} else {\n"
                + "    ___item = (3 * 9);\n"
                + "}\n"
                + "if ( true ) {\n"
                + "    ___item = (6 + 8);\n"
                + "    ___item = (6 + 8);\n"
                + "}\n"
                + "if ( false ) {\n"
                + "    ___item = (6 + 8);\n"
                + "    ___item = (6 + 8);\n"
                + "    ___item = (3 * 9);\n"
                + "} else if ( true ) {\n"
                + "    ___item = (3 * 9);\n"
                + "    ___item = (3 * 9);\n"
                + "} else {\n"
                + "    ___item = (3 * 9);\n"
                + "}";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/stmt/if_stmt.xml", false);
    }

    @Test
    public void ifStmt1() throws Exception {
        final String a = "\nif((((5+7))==((5+7)))>=((((5+7))==((5+7)))&&(((5+7))<=((5+7))))){\n}";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/stmt/if_stmt1.xml", false);
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

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/stmt/if_stmt2.xml", false);
    }

    //
    public void ifStmt3() throws Exception {
        final String a =
            "\nif ( true ) {\n"
                + "    if ( false ) {\n"
                + "    }\n"
                + "}\n"
                + "if ( false ) {\n"
                + "    ___item = 6 + 8;\n"
                + "    ___item = 6 + 8;\n"
                + "} else {\n"
                + "    ___item = 3 * 9;\n"
                + "}";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/stmt/if_stmt3.xml", false);
    }

    @Test
    public void reverseTransformation() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/syntax/stmt/if_stmt.xml");
    }

    @Test
    public void reverseTransformation1() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/syntax/stmt/if_stmt1.xml");
    }

    @Test
    public void reverseTransformation2() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/syntax/stmt/if_stmt2.xml");
    }

    @Test
    public void reverseTransformation3() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/syntax/stmt/if_stmt3.xml");
    }
}