package de.fhg.iais.roberta.syntax.stmt;

import org.junit.Ignore;
import org.junit.Test;

import de.fhg.iais.roberta.Ev3LejosAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class ForStmtTest extends Ev3LejosAstTest {

    @Test
    public void forStmt() throws Exception {
        String a =
            "\nfor ( float ___k0 = 0; ___k0 < 10; ___k0+=1 ) {\n"
                + "}\n"
                + "for ( float ___k1 = 0; ___k1 < 10; ___k1+=1 ) {\n"
                + "    System.out.println(\"15\");\n"
                + "    System.out.println(\"15\");\n"
                + "}\n"
                + "for ( float ___k2 = 0; ___k2 < 10; ___k2+=1 ) {\n"
                + "    for ( float ___k3 = 0; ___k3 < 10; ___k3+=1 ) {\n"
                + "        System.out.println(\"15\");\n"
                + "        System.out.println(\"15\");\n"
                + "    }\n"
                + "}}";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/stmt/for_stmt.xml", false);
    }

    // TODO Invalid test
    @Ignore
    @Test
    public void forStmt1() throws Exception {
        String a =
            "for ( float k0 = 0; k0 < 10; k0+=1 ) {item3 += String.valueOf(\"Proba\");item3 += String.valueOf(\"Proba1\");for ( float k1 = 0; k1 < 10; k1+=1 ) {}}}";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/control/repeat_stmt.xml", false);
    }

    @Test
    public void reverseTransformation() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/syntax/stmt/for_stmt.xml");
    }
}