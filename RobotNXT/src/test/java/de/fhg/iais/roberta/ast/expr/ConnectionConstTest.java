package de.fhg.iais.roberta.ast.expr;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.testutil.Helper;

public class ConnectionConstTest {

    @Test
    public void make() throws Exception {
        String a = "BlockAST [project=[[Location [x=63, y=313], ConnectConst [0]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/connection/connection_const.xml"));
    }

    @Test
    public void reverseTransformatin() throws Exception {
        Helper.assertTransformationIsOk("/ast/connection/connection_const.xml");
    }

    @Test
    public void reverseTransformatin1() throws Exception {
        Helper.assertTransformationIsOk("/ast/connection/connection_const.xml");
    }

    @Test
    public void reverseTransformatin2() throws Exception {
        Helper.assertTransformationIsOk("/ast/connection/connection_const.xml");
    }
}
