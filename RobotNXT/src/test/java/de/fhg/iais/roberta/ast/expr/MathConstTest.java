package de.fhg.iais.roberta.ast.expr;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.syntax.expr.Assoc;
import de.fhg.iais.roberta.syntax.expr.MathConst;
import de.fhg.iais.roberta.syntax.expr.MathConst.Const;
import de.fhg.iais.roberta.testutil.Helper;
import de.fhg.iais.roberta.transformer.Jaxb2BlocklyProgramTransformer;
import de.fhg.iais.roberta.util.dbc.DbcException;

public class MathConstTest {

    @Test
    public void make() throws Exception {
        String a = "BlockAST [project=[[Location [x=131, y=-615], MathConst [E]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/math/math_constant1.xml"));
    }

    @Test
    public void getMathConst() throws Exception {
        Jaxb2BlocklyProgramTransformer<Void> transformer = Helper.generateTransformer("/ast/math/math_constant1.xml");
        MathConst<Void> mathConst = (MathConst<Void>) transformer.getTree().get(0).get(1);
        Assert.assertEquals(Const.E, mathConst.getMathConst());
    }

    @Test
    public void getPresedance() throws Exception {
        Jaxb2BlocklyProgramTransformer<Void> transformer = Helper.generateTransformer("/ast/math/math_constant1.xml");
        MathConst<Void> mathConst = (MathConst<Void>) transformer.getTree().get(0).get(1);
        Assert.assertEquals(999, mathConst.getPrecedence());
    }

    @Test
    public void getAssoc() throws Exception {
        Jaxb2BlocklyProgramTransformer<Void> transformer = Helper.generateTransformer("/ast/math/math_constant1.xml");
        MathConst<Void> mathConst = (MathConst<Void>) transformer.getTree().get(0).get(1);
        Assert.assertEquals(Assoc.NONE, mathConst.getAssoc());
    }

    @Test
    public void reverseTransformatin() throws Exception {
        Helper.assertTransformationIsOk("/ast/math/math_constant.xml");
    }

    @Test
    public void reverseTransformatin1() throws Exception {
        Helper.assertTransformationIsOk("/ast/math/math_constant1.xml");
    }

    @Test
    public void reverseTransformatin2() throws Exception {
        Helper.assertTransformationIsOk("/ast/math/math_constant2.xml");
    }

    @Test
    public void reverseTransformatin3() throws Exception {
        Helper.assertTransformationIsOk("/ast/math/math_constant3.xml");
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
