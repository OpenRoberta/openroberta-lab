package de.fhg.iais.roberta.ast.expr;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.syntax.expr.Assoc;
import de.fhg.iais.roberta.syntax.expr.Unary;
import de.fhg.iais.roberta.testutil.Helper;
import de.fhg.iais.roberta.transformer.Jaxb2BlocklyProgramTransformer;
import de.fhg.iais.roberta.util.dbc.DbcException;

public class UnaryTest {

    @Test
    public void make() throws Exception {
        String a = "BlockAST [project=[[Location [x=-46, y=111], Unary [NEG, NumConst [10]]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/math/math_single1.xml"));
    }

    @Test
    public void getOp() throws Exception {
        Jaxb2BlocklyProgramTransformer<Void> transformer = Helper.generateTransformer("/ast/math/math_single1.xml");
        Unary<Void> unary = (Unary<Void>) transformer.getTree().get(0).get(1);
        Assert.assertEquals(Unary.Op.NEG, unary.getOp());
    }

    @Test
    public void getExpr() throws Exception {
        Jaxb2BlocklyProgramTransformer<Void> transformer = Helper.generateTransformer("/ast/math/math_single1.xml");
        Unary<Void> unary = (Unary<Void>) transformer.getTree().get(0).get(1);
        Assert.assertEquals("NumConst [10]", unary.getExpr().toString());
    }

    @Test
    public void getPresedance() throws Exception {
        Jaxb2BlocklyProgramTransformer<Void> transformer = Helper.generateTransformer("/ast/math/math_single1.xml");
        Unary<Void> unary = (Unary<Void>) transformer.getTree().get(0).get(1);
        Assert.assertEquals(10, unary.getPrecedence());
    }

    @Test
    public void getAssoc() throws Exception {
        Jaxb2BlocklyProgramTransformer<Void> transformer = Helper.generateTransformer("/ast/math/math_single1.xml");
        Unary<Void> unary = (Unary<Void>) transformer.getTree().get(0).get(1);
        Assert.assertEquals(Assoc.LEFT, unary.getAssoc());
    }

    @Test
    public void getOpSymbol() throws Exception {
        Jaxb2BlocklyProgramTransformer<Void> transformer = Helper.generateTransformer("/ast/math/math_single1.xml");
        Unary<Void> unary = (Unary<Void>) transformer.getTree().get(0).get(1);
        Assert.assertEquals("-", unary.getOp().getOpSymbol());
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
