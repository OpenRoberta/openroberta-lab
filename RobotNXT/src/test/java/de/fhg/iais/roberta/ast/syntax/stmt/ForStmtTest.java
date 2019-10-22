package de.fhg.iais.roberta.ast.syntax.stmt;

import org.junit.Ignore;
import org.junit.Test;

import de.fhg.iais.roberta.NxtAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class ForStmtTest extends NxtAstTest {

    // TODO invalid test
    @Ignore
    @Test
    public void forStmt1() throws Exception {
        String a = "for ( int ___k0 = 0; ___k0 < 10; ___k0+=1 ) {___item3 += \"Proba\";___item3 += \"Proba1\";for ( int ___k1 = 0; ___k1 < 10; ___k1+=1 ) {}}";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/ast/control/repeat_stmt.xml", false);
    }

    @Test
    public void reverseTransformation() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/syntax/stmt/for_stmt.xml");
    }
}