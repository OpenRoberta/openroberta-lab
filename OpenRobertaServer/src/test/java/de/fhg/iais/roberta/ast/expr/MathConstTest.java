package de.fhg.iais.roberta.ast.expr;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.codeGeneration.Helper;
import de.fhg.iais.roberta.ast.syntax.expr.Assoc;
import de.fhg.iais.roberta.ast.syntax.expr.MathConst;
import de.fhg.iais.roberta.ast.syntax.expr.MathConst.Const;
import de.fhg.iais.roberta.ast.transformer.JaxbBlocklyProgramTransformer;
import de.fhg.iais.roberta.dbc.DbcException;

public class MathConstTest {

    @Test
    public void make() throws Exception {
        String a = "BlockAST [project=[[Location [x=131, y=-615], MathConst [E]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/math/math_constant1.xml"));
    }

    @Test
    public void getMathConst() throws Exception {
        JaxbBlocklyProgramTransformer<Void> transformer = Helper.generateTransformer("/ast/math/math_constant1.xml");
        MathConst<Void> mathConst = (MathConst<Void>) transformer.getTree().get(1);
        Assert.assertEquals(Const.E, mathConst.getMathConst());
    }

    @Test
    public void getPresedance() throws Exception {
        JaxbBlocklyProgramTransformer<Void> transformer = Helper.generateTransformer("/ast/math/math_constant1.xml");
        MathConst<Void> mathConst = (MathConst<Void>) transformer.getTree().get(1);
        Assert.assertEquals(999, mathConst.getPrecedence());
    }

    @Test
    public void getAssoc() throws Exception {
        JaxbBlocklyProgramTransformer<Void> transformer = Helper.generateTransformer("/ast/math/math_constant1.xml");
        MathConst<Void> mathConst = (MathConst<Void>) transformer.getTree().get(1);
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
