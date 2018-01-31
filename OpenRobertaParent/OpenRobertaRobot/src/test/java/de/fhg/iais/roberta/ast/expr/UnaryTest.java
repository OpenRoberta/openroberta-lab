package de.fhg.iais.roberta.ast.expr;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.syntax.lang.expr.Assoc;
import de.fhg.iais.roberta.syntax.lang.expr.Unary;
import de.fhg.iais.roberta.transformer.Jaxb2BlocklyProgramTransformer;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.util.test.GenericHelperForXmlTest;
import de.fhg.iais.roberta.util.test.AbstractHelperForXmlTest;

public class UnaryTest {
    AbstractHelperForXmlTest h = new GenericHelperForXmlTest();

    @Test
    public void make() throws Exception {
        String a = "BlockAST [project=[[Location [x=-46, y=111], Unary [NEG, NumConst [10]]]]]";

        Assert.assertEquals(a, this.h.generateTransformerString("/ast/math/math_single1.xml"));
    }

    @Test
    public void getOp() throws Exception {
        Jaxb2BlocklyProgramTransformer<Void> transformer = this.h.generateTransformer("/ast/math/math_single1.xml");
        Unary<Void> unary = (Unary<Void>) transformer.getTree().get(0).get(1);
        Assert.assertEquals(Unary.Op.NEG, unary.getOp());
    }

    @Test
    public void getExpr() throws Exception {
        Jaxb2BlocklyProgramTransformer<Void> transformer = this.h.generateTransformer("/ast/math/math_single1.xml");
        Unary<Void> unary = (Unary<Void>) transformer.getTree().get(0).get(1);
        Assert.assertEquals("NumConst [10]", unary.getExpr().toString());
    }

    @Test
    public void getPresedance() throws Exception {
        Jaxb2BlocklyProgramTransformer<Void> transformer = this.h.generateTransformer("/ast/math/math_single1.xml");
        Unary<Void> unary = (Unary<Void>) transformer.getTree().get(0).get(1);
        Assert.assertEquals(10, unary.getPrecedence());
    }

    @Test
    public void getAssoc() throws Exception {
        Jaxb2BlocklyProgramTransformer<Void> transformer = this.h.generateTransformer("/ast/math/math_single1.xml");
        Unary<Void> unary = (Unary<Void>) transformer.getTree().get(0).get(1);
        Assert.assertEquals(Assoc.LEFT, unary.getAssoc());
    }

    @Test(expected = DbcException.class)
    public void invalid() {
        Unary.Op.get("");
    }

    @Test(expected = DbcException.class)
    public void invalid1() {
        Unary.Op.get(null);
    }

    @Test(expected = DbcException.class)
    public void invalid2() {
        Unary.Op.get("asdf");
    }
}
