package de.fhg.iais.roberta.ast.expr;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.codeGeneration.Helper;
import de.fhg.iais.roberta.ast.syntax.expr.Assoc;
import de.fhg.iais.roberta.ast.syntax.expr.Unary;
import de.fhg.iais.roberta.ast.transformer.JaxbTransformer;
import de.fhg.iais.roberta.dbc.DbcException;

public class UnaryTest {

    @Test
    public void make() throws Exception {
        String a = "BlockAST [project=[[Unary [NEG, NumConst [10]]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/math/math_single1.xml"));
    }

    @Test
    public void getOp() throws Exception {
        JaxbTransformer transformer = Helper.generateTransformer("/ast/math/math_single1.xml");

        Unary unary = (Unary) transformer.getTree().get(0);

        Assert.assertEquals(Unary.Op.NEG, unary.getOp());
    }

    @Test
    public void getExpr() throws Exception {
        JaxbTransformer transformer = Helper.generateTransformer("/ast/math/math_single1.xml");

        Unary unary = (Unary) transformer.getTree().get(0);

        Assert.assertEquals("NumConst [10]", unary.getExpr().toString());
    }

    @Test
    public void getPresedance() throws Exception {
        JaxbTransformer transformer = Helper.generateTransformer("/ast/math/math_single1.xml");

        Unary unary = (Unary) transformer.getTree().get(0);

        Assert.assertEquals(10, unary.getPrecedence());
    }

    @Test
    public void getAssoc() throws Exception {
        JaxbTransformer transformer = Helper.generateTransformer("/ast/math/math_single1.xml");

        Unary unary = (Unary) transformer.getTree().get(0);

        Assert.assertEquals(Assoc.LEFT, unary.getAssoc());
    }

    @Test
    public void getOpSymbol() throws Exception {
        JaxbTransformer transformer = Helper.generateTransformer("/ast/math/math_single1.xml");

        Unary unary = (Unary) transformer.getTree().get(0);

        Assert.assertEquals("-", unary.getOp().getOpSymbol());
    }

    @Test(expected = DbcException.class)
    public void invalid() {
        Unary.Op op = Unary.Op.get("");
    }

    @Test(expected = DbcException.class)
    public void invalid1() {
        Unary.Op op = Unary.Op.get(null);
    }

    @Test(expected = DbcException.class)
    public void invalid2() {
        Unary.Op op = Unary.Op.get("asdf");
    }
}
