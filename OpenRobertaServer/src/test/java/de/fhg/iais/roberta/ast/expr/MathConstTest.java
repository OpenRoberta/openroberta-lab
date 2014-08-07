package de.fhg.iais.roberta.ast.expr;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.expr.Assoc;
import de.fhg.iais.roberta.ast.syntax.expr.MathConst;
import de.fhg.iais.roberta.ast.syntax.expr.MathConst.Const;
import de.fhg.iais.roberta.ast.transformer.JaxbTransformer;
import de.fhg.iais.roberta.helper.Helper;

public class MathConstTest {

    @Test
    public void make() throws Exception {
        String a = "BlockAST [project=[[MathConst [E]]]]";

        Assert.assertEquals(a, Helper.generateASTString("/ast/math/math_constant1.xml"));
    }

    @Test
    public void getMathConst() throws Exception {
        JaxbTransformer transformer = Helper.generateAST("/ast/math/math_constant1.xml");

        MathConst mathConst = (MathConst) transformer.getTree().get(0);

        Assert.assertEquals(Const.E, mathConst.getMathConst());
    }

    @Test
    public void getPresedance() throws Exception {
        JaxbTransformer transformer = Helper.generateAST("/ast/math/math_constant1.xml");

        MathConst mathConst = (MathConst) transformer.getTree().get(0);

        Assert.assertEquals(999, mathConst.getPrecedence());
    }

    @Test
    public void getAssoc() throws Exception {
        JaxbTransformer transformer = Helper.generateAST("/ast/math/math_constant1.xml");

        MathConst mathConst = (MathConst) transformer.getTree().get(0);

        Assert.assertEquals(Assoc.NONE, mathConst.getAssoc());
    }
}
