package de.fhg.iais.roberta.syntax.stmt;

import org.junit.Ignore;
import org.junit.Test;

import de.fhg.iais.roberta.Ev3LejosAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class IfStmtTest extends Ev3LejosAstTest {

    @Ignore
    public void ifStmt() throws Exception {
        String a =
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

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/stmt/if_stmt.xml", false);
    }

    @Test
    public void ifStmt1() throws Exception {
        String a = "\nif ( ( (5 + 7) == (5 + 7) ) >= ( ((5 + 7) == (5 + 7)) && ((5 + 7) <= (5 + 7) )) ) {\n}}";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/stmt/if_stmt1.xml", false);
    }

    @Test
    public void ifStmt2() throws Exception {
        String a =
            "\nif ( true ) {\n"
                + "    System.out.println(\"1\");\n"
                + "    System.out.println(\"8\");\n"
                + "} else if ( false ) {\n"
                + "    System.out.println(\"2\");\n"
                + "} else {\n"
                + "    System.out.println(\"3\");\n"
                + "}\n"
                + "if ( true ) {\n"
                + "    System.out.println(\" 1\");\n"
                + "} else {\n"
                + "    System.out.println(\" else\");\n"
                + "    System.out.println(0);\n"
                + "}}";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/stmt/if_stmt2.xml", false);
    }

    @Test
    public void ifStmt3() throws Exception {
        String a =
            "\nif ( true ) {\n"
                + "    if ( false ) {\n"
                + "    }\n"
                + "}\n"
                + "if ( false ) {\n"
                + "    ___item = 6 + 8;\n"
                + "    ___item = 6 + 8;\n"
                + "} else {\n"
                + "    ___item = 3 * 9;\n"
                + "}}";

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