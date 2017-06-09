package de.fhg.iais.roberta.ast.stmt;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.util.test.GenericHelper;
import de.fhg.iais.roberta.util.test.Helper;

public class WaitTimeStmtTest {
    Helper h = new GenericHelper();

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
