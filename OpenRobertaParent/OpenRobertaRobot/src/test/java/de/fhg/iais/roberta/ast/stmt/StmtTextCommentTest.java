package de.fhg.iais.roberta.ast.stmt;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.util.test.GenericHelperForXmlTest;
import de.fhg.iais.roberta.util.test.AbstractHelperForXmlTest;

public class StmtTextCommentTest {
    AbstractHelperForXmlTest h = new GenericHelperForXmlTest();

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
