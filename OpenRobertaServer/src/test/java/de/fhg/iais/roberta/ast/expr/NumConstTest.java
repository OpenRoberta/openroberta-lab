package de.fhg.iais.roberta.ast.expr;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.codeGeneration.Helper;
import de.fhg.iais.roberta.ast.syntax.expr.Assoc;
import de.fhg.iais.roberta.ast.syntax.expr.NumConst;
import de.fhg.iais.roberta.ast.transformer.JaxbTransformer;

public class NumConstTest {

    @Test
    public void make() throws Exception {
        String a = "BlockAST [project=[[NumConst [0]]]]";
        Assert.assertEquals(a, Helper.generateTransformerString("/ast/math/math_num_constant.xml"));
    }

    @Test
    public void getValue() throws Exception {
        JaxbTransformer<Void> transformer = Helper.generateTransformer("/ast/math/math_num_constant.xml");
        NumConst<Void> numConst = (NumConst<Void>) transformer.getTree().get(0);
        Assert.assertEquals("0", numConst.getValue());
    }

    @Test
    public void getPresedance() throws Exception {
        JaxbTransformer<Void> transformer = Helper.generateTransformer("/ast/math/math_num_constant.xml");
        NumConst<Void> numConst = (NumConst<Void>) transformer.getTree().get(0);
        Assert.assertEquals(999, numConst.getPrecedence());
    }

    @Test
    public void getAssoc() throws Exception {
        JaxbTransformer<Void> transformer = Helper.generateTransformer("/ast/math/math_num_constant.xml");
        NumConst<Void> numConst = (NumConst<Void>) transformer.getTree().get(0);
        Assert.assertEquals(Assoc.NONE, numConst.getAssoc());
    }
}
