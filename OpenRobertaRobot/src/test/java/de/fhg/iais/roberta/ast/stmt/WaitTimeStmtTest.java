package de.fhg.iais.roberta.ast.stmt;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.util.test.GenericHelperForXmlTest;
import de.fhg.iais.roberta.util.test.AbstractHelperForXmlTest;

public class WaitTimeStmtTest {
    AbstractHelperForXmlTest h = new GenericHelperForXmlTest();

    @Test
    public void test() throws Exception {
        final String a = "BlockAST [project=[[Location [x=75, y=116], WaitTimeStmt [time=NumConst [500]]]]]";
        Assert.assertEquals(a, this.h.generateTransformerString("/ast/control/wait_time_stmt.xml"));
    }

    @Test
    public void reverseTransformation() throws Exception {
        this.h.assertTransformationIsOk("/ast/control/wait_time_stmt.xml");
    }
}
