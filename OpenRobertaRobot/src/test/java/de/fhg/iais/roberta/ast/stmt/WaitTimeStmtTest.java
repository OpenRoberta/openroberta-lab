package de.fhg.iais.roberta.ast.stmt;

import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class WaitTimeStmtTest extends AstTest {

    @Test
    public void test() throws Exception {
        final String a = "BlockAST [project=[[Location [x=75, y=116], WaitTimeStmt [time=NumConst [500]]]]]";
        UnitTestHelper.checkProgramAstEquality(testFactory, a, "/ast/control/wait_time_stmt.xml");
    }

    @Test
    public void reverseTransformation() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/control/wait_time_stmt.xml");
    }
}
