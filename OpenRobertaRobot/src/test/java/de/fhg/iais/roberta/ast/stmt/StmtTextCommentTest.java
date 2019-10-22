package de.fhg.iais.roberta.ast.stmt;

import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class StmtTextCommentTest extends AstTest {

    @Test
    public void test() throws Exception {
        final String a = "BlockAST [project=[[Location [x=-846, y=39], \n" + "StmtTextComment [This should be a usefull comment]]]]";
        UnitTestHelper.checkProgramAstEquality(testFactory, a, "/ast/text/text_comment_stmt.xml");
    }

    @Test
    public void reverseTransformation() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/text/text_comment_stmt.xml");
    }
}
