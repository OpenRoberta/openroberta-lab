package de.fhg.iais.roberta.ast.expr;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.expr.Assoc;
import de.fhg.iais.roberta.ast.syntax.expr.NullConst;

public class NullConstTest {

    @Test
    public void make() throws Exception {
        NullConst<Void> nullConst = NullConst.make();
        String a = "NullConst [null]";
        Assert.assertEquals(a, nullConst.toString());
    }

    @Test
    public void getValue() throws Exception {
        NullConst<Void> nullConst = NullConst.make();
        Assert.assertEquals(null, nullConst.getValue());
    }

    @Test
    public void getPresedance() throws Exception {
        NullConst<Void> nullConst = NullConst.make();
        Assert.assertEquals(999, nullConst.getPrecedence());
    }

    @Test
    public void getAssoc() throws Exception {
        NullConst<Void> nullConst = NullConst.make();
        Assert.assertEquals(Assoc.NONE, nullConst.getAssoc());
    }
}
