package de.fhg.iais.roberta.ast.expr;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.codeGeneration.Helper;
import de.fhg.iais.roberta.ast.syntax.expr.Assoc;
import de.fhg.iais.roberta.ast.syntax.expr.MathConst;
import de.fhg.iais.roberta.ast.syntax.expr.MathConst.Const;
import de.fhg.iais.roberta.ast.transformer.JaxbTransformer;
import de.fhg.iais.roberta.dbc.DbcException;

public class MathConstTest {

    @Test
    public void make() throws Exception {
        String a = "BlockAST [project=[[MathConst [E]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/math/math_constant1.xml"));
    }

    @Test
    public void getMathConst() throws Exception {
        JaxbTransformer<Void> transformer = Helper.generateTransformer("/ast/math/math_constant1.xml");
        MathConst<Void> mathConst = (MathConst<Void>) transformer.getTree().get(0);
        Assert.assertEquals(Const.E, mathConst.getMathConst());
    }

    @Test
    public void getPresedance() throws Exception {
        JaxbTransformer<Void> transformer = Helper.generateTransformer("/ast/math/math_constant1.xml");
        MathConst<Void> mathConst = (MathConst<Void>) transformer.getTree().get(0);
        Assert.assertEquals(999, mathConst.getPrecedence());
    }

    @Test
    public void getAssoc() throws Exception {
        JaxbTransformer<Void> transformer = Helper.generateTransformer("/ast/math/math_constant1.xml");
        MathConst<Void> mathConst = (MathConst<Void>) transformer.getTree().get(0);
        Assert.assertEquals(Assoc.NONE, mathConst.getAssoc());
    }

    @Test(expected = DbcException.class)
    public void invalid() {
        MathConst.Const.get("");
    }

    @Test(expected = DbcException.class)
    public void invalid1() {
        MathConst.Const.get(null);
    }

    @Test(expected = DbcException.class)
    public void invalid2() {
        MathConst.Const.get("asdf");
    }
}
