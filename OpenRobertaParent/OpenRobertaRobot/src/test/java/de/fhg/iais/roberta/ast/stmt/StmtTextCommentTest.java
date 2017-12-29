package de.fhg.iais.roberta.ast.stmt;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.util.test.GenericHelper;
import de.fhg.iais.roberta.util.test.Helper;

public class StmtTextCommentTest {
    Helper h = new GenericHelper();

    @Test
    public void test() throws Exception {
        final String a = "BlockAST [project=[[Location [x=-846, y=39], \n" + "StmtTextComment [This should be a usefull comment]]]]";
        Assert.assertEquals(a, this.h.generateTransformerString("/ast/text/text_comment_stmt.xml"));
    }

    @Test
    public void reverseTransformation() throws Exception {
        this.h.assertTransformationIsOk("/ast/text/text_comment_stmt.xml");
    }
}
