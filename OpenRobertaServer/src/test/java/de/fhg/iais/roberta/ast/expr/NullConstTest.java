package de.fhg.iais.roberta.ast.expr;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.codeGeneration.Helper;
import de.fhg.iais.roberta.ast.syntax.expr.Assoc;
import de.fhg.iais.roberta.ast.syntax.expr.NullConst;

public class NullConstTest {

    @Test
    public void make() throws Exception {
        NullConst<Void> nullConst = NullConst.make(null, null);
        String a = "NullConst [null]";
        Assert.assertEquals(a, nullConst.toString());
    }

    @Test
    public void getValue() throws Exception {
        NullConst<Void> nullConst = NullConst.make(null, null);
        Assert.assertEquals(null, nullConst.getValue());
    }

    @Test
    public void getPresedance() throws Exception {
        NullConst<Void> nullConst = NullConst.make(null, null);
        Assert.assertEquals(999, nullConst.getPrecedence());
    }

    @Test
    public void getAssoc() throws Exception {
        NullConst<Void> nullConst = NullConst.make(null, null);
        Assert.assertEquals(Assoc.NONE, nullConst.getAssoc());
    }

    @Test
    public void reverseTransformatin() throws Exception {
        Helper.assertTransformationIsOk("/ast/logic/logic_null.xml");
    }

    @Test
    public void reverseTransformatin1() throws Exception {
        Helper.assertTransformationIsOk("/ast/logic/logic_null1.xml");
    }

    @Test
    public void reverseTransformatin2() throws Exception {
        Helper.assertTransformationIsOk("/ast/logic/logic_null2.xml");
    }

}
